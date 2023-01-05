package com.planet.utils.crash

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.os.Process
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.pm.PackageInfoCompat
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * 当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
class CrashHandler
/**
 * 私有构造函数
 */
private constructor() : Thread.UncaughtExceptionHandler {
    //系统默认的UncaughtException处理类
    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    //程序的App对象
    private var mApplication: Application? = null

    //生命周期监听
    private var mMyActivityLifecycleCallbacks = MyActivityLifecycleCallbacks()

    //用来存储设备信息和异常信息
    private val infos: MutableMap<String, String> = HashMap()

    //用于格式化日期,作为日志文件名的一部分
    private val formatter: DateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)

    //是否是Debug模式
    private var mIsDebug = false

    //是否重启APP
    private var mIsRestartApp = false

    //重启APP时间
    private var mRestartTime: Long = 0

    //重启后的第一个Activity class文件
    private var mClassOfFirstActivity: Class<*>? = null

    //是否已经toast
    private var hasToast = false
    fun init(application: Application, isDebug: Boolean, isRestartApp: Boolean, restartTime: Long, classOfFirstActivity: Class<*>?) {
        mIsRestartApp = isRestartApp
        mRestartTime = restartTime
        mClassOfFirstActivity = classOfFirstActivity
        initCrashHandler(application, isDebug)
    }

    fun init(application: Application, isDebug: Boolean) {
        initCrashHandler(application, isDebug)
    }

    /**
     * 初始化
     *
     * @since V1.0
     */
    private fun initCrashHandler(application: Application, isDebug: Boolean) {
        mIsDebug = isDebug
        mApplication = application
        mApplication!!.registerActivityLifecycleCallbacks(mMyActivityLifecycleCallbacks)
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val isHandle = handleException(ex)
        if (!isHandle && mDefaultHandler != null) {
            //先自己处理没，然后再交由系统处理，这样Bugly和其他日志收集工具也可以继续处理错误日志。
            handleException(ex)
            //系统继续处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                //给Toast留出时间
                Thread.sleep(2800)
            } catch (e: InterruptedException) {
                Log.e(TAG, "uncaughtException() InterruptedException:$e")
            }
            if (mIsRestartApp) {
                //利用系统时钟进行重启任务
                val mgr = mApplication!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                try {
                    val intent = Intent(mApplication, mClassOfFirstActivity)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val restartIntent = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                    mgr[AlarmManager.RTC, System.currentTimeMillis() + mRestartTime] = restartIntent // x秒钟后重启应用
                } catch (e: Exception) {
                    Log.e(TAG, "first class error:$e")
                }
            }
            mMyActivityLifecycleCallbacks.removeAllActivities()
            Process.killProcess(Process.myPid())
            exitProcess(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    @SuppressLint("ShowToast")
    private fun handleException(ex: Throwable?): Boolean {
        if (!hasToast) {
            Thread {
                try {
                    Looper.prepare()
                    val toast: Toast?
                    if (mCustomToast == null) {
                        toast = Toast.makeText(mApplication, mCrashTip, Toast.LENGTH_LONG)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                    } else {
                        toast = mCustomToast
                    }
                    toast!!.show()
                    Looper.loop()
                    hasToast = true
                } catch (e: Exception) {
                    Log.e(TAG, "handleException Toast error$e")
                }
            }.start()
        }
        if (ex == null) {
            return false
        }
        if (mIsDebug) {
            // 收集设备参数信息
            collectDeviceInfo()
            // 保存日志文件
            saveCatchInfo2File(ex)
        }
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @since V1.0
     */
    private fun collectDeviceInfo() {
        try {
            val pm = mApplication!!.packageManager
            val pi = pm.getPackageInfo(mApplication!!.packageName, PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = PackageInfoCompat.getLongVersionCode(pi).toString()
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "collectDeviceInfo() an error occurred when collect package info NameNotFoundException:${e.message.toString()}")
        }
        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field[null]!!.toString()
                Log.i(TAG, field.name + " : " + field[null])
            } catch (e: Exception) {
                Log.e(TAG, "collectDeviceInfo() an error occurred when collect crash info Exception:${e.message.toString()}")
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 文件名称
     */
    private fun saveCatchInfo2File(ex: Throwable) {
        val sb = StringBuffer()
        sb.append("------------------------start------------------------------\n")
        for ((key, value) in infos) {
            sb.append("$key=$value\n")
        }
        sb.append(getCrashInfo(ex))
        sb.append("\n------------------------end------------------------------")
        try {
            val timestamp = System.currentTimeMillis()
            val time = formatter.format(Date())
            val fileName = "crash-$time-$timestamp.txt"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path = mApplication!!.getExternalFilesDirs(null)[0].absolutePath + File.separator + "crash/"
                val dir = File(path)
                if (!dir.exists()) dir.mkdirs()
                // 创建新的文件
                if (!dir.exists()) dir.createNewFile()
                val fos = FileOutputStream(path + fileName)
                fos.write(sb.toString().toByteArray())
                // 答出log日志到控制台
                logcatCrashInfo(path + fileName)
                fos.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "saveCatchInfo2File() an error occurred while writing file... Exception:")
        }
    }

    /**
     * 将捕获的导致崩溃的错误信息保存在sdcard 和输出到LogCat中
     *
     * @param fileName
     * @since V1.0
     */
    private fun logcatCrashInfo(fileName: String) {
        if (!File(fileName).exists()) {
            Log.e(TAG, "LogcatCrashInfo() 日志文件不存在")
            return
        }
        var fis: FileInputStream? = null
        var reader: BufferedReader? = null
        var s: String?
        try {
            fis = FileInputStream(fileName)
            reader = BufferedReader(InputStreamReader(fis, "GBK"))
            while (true) {
                s = reader.readLine()
                if (s == null) break
                Log.e(TAG, s)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally { // 关闭流
            try {
                reader!!.close()
                fis!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 得到程序崩溃的详细信息
     */
    private fun getCrashInfo(ex: Throwable): String {
        val result: Writer = StringWriter()
        val printWriter = PrintWriter(result)
        ex.stackTrace = ex.stackTrace
        ex.printStackTrace(printWriter)
        printWriter.close()
        return result.toString()
    }

    companion object {
        //TAG
        const val TAG = "CrashHandler"

        //自定义Toast
        private var mCustomToast: Toast? = null

        //提示文字
        private var mCrashTip = "程序出现异常，即将退出。"

        //CrashHandler实例
        private var mCrashHandler: CrashHandler? = null

        /**
         * 获取CrashHandler实例 ,单例模式
         */
        val instance: CrashHandler
            get() {
                if (mCrashHandler == null) mCrashHandler = CrashHandler()
                return mCrashHandler as CrashHandler
            }

        fun setCloseAnimation(closeAnimation: Int) {
            MyActivityLifecycleCallbacks.sAnimationId = closeAnimation
        }

        fun setCustomToast(customToast: Toast?) {
            mCustomToast = customToast
        }

        fun setCrashTip(crashTip: String) {
            mCrashTip = crashTip
        }
    }
}