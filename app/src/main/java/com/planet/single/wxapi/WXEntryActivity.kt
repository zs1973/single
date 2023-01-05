package com.planet.single.wxapi
//微信登录集成在lib中时，微信回调时会因为包名问题找不到此类，将包名修改为与项目包名一致可解决此问题

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.planet.core.ui.ImmersionActivity
import com.planet.network.util.Status
import com.planet.single.databinding.ActivityWxentryBinding
import com.planet.utils.AppUtils
import com.planet.utils.LogUtils
import com.planet.utils.ToastUtils
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint

/**
 *作者：张硕
 *日期：2022/04/06
 *邮箱：305562245@qq.com
 *谨记：想要完美时，完美即已不存在。
 *描述：
 **/
@AndroidEntryPoint
class WXEntryActivity : ImmersionActivity<ActivityWxentryBinding>(), IWXAPIEventHandler {

    private lateinit var mWxApi: IWXAPI
    private val mVm: WxEntryViewModel by viewModels()
    private val mTag = javaClass.simpleName


    override fun doBusiness() {
    }

    override fun initViewBinding(): ActivityWxentryBinding {
        return ActivityWxentryBinding.inflate(layoutInflater)
    }

    override fun observerData() {
        mVm.loginStatus.observe(this) {
            when (it) {
                Status.Error -> {
                    finish()
                }
                Status.Loading -> {}
                Status.Success -> {
                    //登录成功之后的逻辑
                    //val intent = Intent(this, MainActivity::class.java)
                    //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    //startActivity(intent)
                    //finish()
                }
            }
        }

        mVm.toast.observe(this) {
            ToastUtils.showShort(it)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        mWxApi.handleIntent(intent, this)
    }

    override fun initData(savedInstanceState: Bundle?) {
        mWxApi = if (AppUtils.isAppDebug()) {
            WXAPIFactory.createWXAPI(this, "微信开放平台申请的app_id", false)
        } else {
            WXAPIFactory.createWXAPI(this, "微信开放平台申请的app_id", false)
        }
        try {
            mWxApi.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun listeners() {
        TODO("Not yet implemented")
    }

    override fun onReq(req: BaseReq) {
        ToastUtils.showShort(req.transaction)
        finish()
    }

    /**
     * 微信回调-微信返回给我们的应用消息时回调这个方法
     * @param resp BaseResp
     */
    override fun onResp(resp: BaseResp) {
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val sar = resp as SendAuth.Resp
                LogUtils.loge(mTag, sar.code)
                //登录我方服务器，获取微信用户信息
                mVm.loginTimeKeeperServer(sar.code)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                ToastUtils.showShort("取消登录")
                finish()
            }
            BaseResp.ErrCode.ERR_SENT_FAILED -> {
                ToastUtils.showShort("发送失败")
                finish()
            }
            else -> {
                ToastUtils.showShort(resp.errStr)
                finish()
            }
        }
    }
}