package com.planet.floatingview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;

import com.planet.floatingview.draggable.BaseDraggable;
import com.planet.floatingview.draggable.MovingDraggable;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/XToast
 * time   : 2019/01/04
 * desc   : 悬浮窗框架
 * doc    : https://developer.android.google.cn/reference/android/view/WindowManager.html
 *        : https://developer.android.google.cn/reference/kotlin/android/view/WindowManager.LayoutParams?hl=en
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class FloatingView implements Runnable {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 根布局
     */
    private ViewGroup mDecorView;
    /**
     * 悬浮窗口
     */
    private WindowManager mWindowManager;
    /**
     * 窗口参数
     */
    private WindowManager.LayoutParams mWindowParams;
    /**
     * 当前是否已经显示
     */
    private boolean mShowing;
    /**
     * 窗口显示时长
     */
    private int mDuration;
    /**
     * Toast 生命周期管理
     */
    private ActivityLifecycle mLifecycle;
    /**
     * 自定义拖动处理
     */
    private BaseDraggable mDraggable;
    /**
     * 吐司显示和取消监听
     */
    private OnLifecycle mListener;

    /**
     * 创建一个局部悬浮窗
     */
    public FloatingView(Activity activity) {
        this((Context) activity);
        if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0 ||
                (activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
            // 如果当前 Activity 是全屏模式，那么需要添加这个标记，否则会导致 WindowManager 在某些机型上移动不到状态栏的位置上
            // 如果不想让状态栏显示的时候把 WindowManager 顶下来，可以添加 FLAG_LAYOUT_IN_SCREEN，但是会导致软键盘无法调整窗口位置
            addWindowFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        // 跟随 Activity 的生命周期
        mLifecycle = new ActivityLifecycle(this, activity);
    }

    /**
     * 创建一个全局悬浮窗
     */
    public FloatingView(Application application) {
        this((Context) application);
        // 设置成全局的悬浮窗，注意需要先申请悬浮窗权限，推荐使用：https://github.com/getActivity/XXPermissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setWindowType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            setWindowType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    private FloatingView(Context context) {
        mContext = context;
        mDecorView = new WindowLayout(context);
        mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        // 配置一些默认的参数
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = android.R.style.Animation_Toast;
        mWindowParams.packageName = context.getPackageName();
        // 设置触摸外层布局（除 WindowManager 外的布局，默认是 WindowManager 显示的时候外层不可触摸）
        // 需要注意的是设置了 FLAG_NOT_TOUCH_MODAL 必须要设置 FLAG_NOT_FOCUSABLE，否则就会导致用户按返回键无效
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    /**
     * 设置宽度
     */
    public FloatingView setWidth(int width) {
        mWindowParams.width = width;
        if (mDecorView.getChildCount() > 0) {
            View contentView = mDecorView.getChildAt(0);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams != null && layoutParams.width != width) {
                layoutParams.width = width;
                contentView.setLayoutParams(layoutParams);
            }
        }
        update();
        return this;
    }

    /**
     * 设置高度
     */
    public FloatingView setHeight(int height) {
        mWindowParams.height = height;
        if (mDecorView.getChildCount() > 0) {
            View contentView = mDecorView.getChildAt(0);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams != null && layoutParams.height != height) {
                layoutParams.height = height;
                contentView.setLayoutParams(layoutParams);
            }
        }
        update();
        return this;
    }

    /**
     * 设置窗口重心
     */
    public FloatingView setGravity(int gravity) {
        mWindowParams.gravity = gravity;
        update();
        return this;
    }

    /**
     * 设置窗口方向
     * <p>
     * 自适应：{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}
     * 横屏：{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
     * 竖屏：{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
     */
    public FloatingView setScreenOrientation(int orientation) {
        mWindowParams.screenOrientation = orientation;
        update();
        return this;
    }

    /**
     * 设置水平偏移量
     */
    public FloatingView setXOffset(int x) {
        mWindowParams.x = x;
        update();
        return this;
    }

    /**
     * 设置垂直偏移量
     */
    public FloatingView setYOffset(int y) {
        mWindowParams.y = y;
        update();
        return this;
    }

    /**
     * 是否外层可触摸
     */
    public FloatingView setOutsideTouchable(boolean touchable) {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        if (touchable) {
            addWindowFlags(flags);
        } else {
            clearWindowFlags(flags);
        }
        update();
        return this;
    }

    /**
     * 设置窗口背景阴影强度
     */
    public FloatingView setBackgroundDimAmount(float amount) {
        if (amount < 0 || amount > 1) {
            throw new IllegalArgumentException("are you ok?");
        }
        mWindowParams.dimAmount = amount;
        int flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        if (amount != 0) {
            addWindowFlags(flags);
        } else {
            clearWindowFlags(flags);
        }
        update();
        return this;
    }

    /**
     * 是否有这个标志位
     */
    public boolean hasWindowFlags(int flags) {
        return (mWindowParams.flags & flags) != 0;
    }

    /**
     * 添加一个标记位
     */
    public FloatingView addWindowFlags(int flags) {
        mWindowParams.flags |= flags;
        update();
        return this;
    }

    /**
     * 移除一个标记位
     */
    public FloatingView clearWindowFlags(int flags) {
        mWindowParams.flags &= ~flags;
        update();
        return this;
    }

    /**
     * 设置标记位
     */
    public FloatingView setWindowFlags(int flags) {
        mWindowParams.flags = flags;
        update();
        return this;
    }

    /**
     * 设置窗口类型
     */
    public FloatingView setWindowType(int type) {
        mWindowParams.type = type;
        update();
        return this;
    }

    /**
     * 设置动画样式
     */
    public FloatingView setAnimStyle(int id) {
        mWindowParams.windowAnimations = id;
        update();
        return this;
    }

    /**
     * 设置软键盘模式
     * <p>
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_UNSPECIFIED}：没有指定状态,系统会选择一个合适的状态或依赖于主题的设置
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_UNCHANGED}：不会改变软键盘状态
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_HIDDEN}：当用户进入该窗口时，软键盘默认隐藏
     * {@link WindowManager.LayoutParams#SOFT_INPUT_STATE_ALWAYS_HIDDEN}：当窗口获取焦点时，软键盘总是被隐藏
     * {@link WindowManager.LayoutParams#SOFT_INPUT_ADJUST_RESIZE}：当软键盘弹出时，窗口会调整大小
     * {@link WindowManager.LayoutParams#SOFT_INPUT_ADJUST_PAN}：当软键盘弹出时，窗口不需要调整大小，要确保输入焦点是可见的
     */
    public FloatingView setSoftInputMode(int mode) {
        mWindowParams.softInputMode = mode;
        // 如果设置了不能触摸，则擦除这个标记，否则会导致无法弹出输入法
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        update();
        return this;
    }

    /**
     * 设置窗口 Token
     */
    public FloatingView setWindowToken(IBinder token) {
        mWindowParams.token = token;
        update();
        return this;
    }

    /**
     * 设置窗口透明度
     */
    public FloatingView setWindowAlpha(float alpha) {
        mWindowParams.alpha = alpha;
        update();
        return this;
    }

    /**
     * 设置垂直间距
     */
    public FloatingView setVerticalMargin(float verticalMargin) {
        mWindowParams.verticalMargin = verticalMargin;
        update();
        return this;
    }

    /**
     * 设置水平间距
     */
    public FloatingView setHorizontalMargin(float horizontalMargin) {
        mWindowParams.horizontalMargin = horizontalMargin;
        update();
        return this;
    }

    /**
     * 设置位图格式
     */
    public FloatingView setBitmapFormat(int format) {
        mWindowParams.format = format;
        update();
        return this;
    }

    /**
     * 设置状态栏的可见性
     */
    public FloatingView setSystemUiVisibility(int systemUiVisibility) {
        mWindowParams.systemUiVisibility = systemUiVisibility;
        update();
        return this;
    }

    /**
     * 设置垂直权重
     */
    public FloatingView setVerticalWeight(float verticalWeight) {
        mWindowParams.verticalWeight = verticalWeight;
        update();
        return this;
    }

    /**
     * 设置挖孔屏下的显示模式
     */
    public FloatingView setLayoutInDisplayCutoutMode(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mWindowParams.layoutInDisplayCutoutMode = mode;
            update();
        }
        return this;
    }

    /**
     * 设置窗口在哪个显示屏上显示
     */
    public FloatingView setPreferredDisplayModeId(int id) {
        mWindowParams.preferredDisplayModeId = id;
        update();
        return this;
    }

    /**
     * 设置窗口标题
     */
    public FloatingView setWindowTitle(CharSequence title) {
        mWindowParams.setTitle(title);
        update();
        return this;
    }

    /**
     * 设置屏幕的亮度
     */
    public FloatingView setScreenBrightness(float screenBrightness) {
        mWindowParams.screenBrightness = screenBrightness;
        update();
        return this;
    }

    /**
     * 设置按键的亮度
     */
    public FloatingView setButtonBrightness(float buttonBrightness) {
        mWindowParams.buttonBrightness = buttonBrightness;
        update();
        return this;
    }

    /**
     * 设置窗口的刷新率
     */
    @SuppressLint("ObsoleteSdkInt")
    public FloatingView setPreferredRefreshRate(int preferredDisplayModeId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindowParams.preferredDisplayModeId = preferredDisplayModeId;
            update();
        }
        return this;
    }

    /**
     * 设置窗口的颜色模式
     */
    public FloatingView setColorMode(int colorMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mWindowParams.setColorMode(colorMode);
            update();
        }
        return this;
    }

    /**
     * 设置窗口高斯模糊半径大小（Android 12 才有的）
     */
    public FloatingView setBlurBehindRadius(int blurBehindRadius) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            mWindowParams.setBlurBehindRadius(blurBehindRadius);
//            addWindowFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
//            update();
//        }
        return this;
    }

    /**
     * 设置随意拖动
     */
    public FloatingView setDraggable() {
        return setDraggable(new MovingDraggable());
    }

    /**
     * 设置拖动规则
     */
    public FloatingView setDraggable(BaseDraggable draggable) {
        // 如果当前是否设置了不可触摸，如果是就擦除掉这个标记
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // 如果当前是否设置了可移动窗口到屏幕之外，如果是就擦除这个标记
        clearWindowFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mDraggable = draggable;
        if (isShowing()) {
            update();
            mDraggable.start(this);
        }
        return this;
    }

    /**
     * 限定显示时长
     */
    public FloatingView setDuration(int duration) {
        mDuration = duration;
        if (isShowing() && mDuration != 0) {
            removeCallbacks(this);
            postDelayed(this, mDuration);
        }
        return this;
    }

    /**
     * 设置生命周期监听
     */
    public FloatingView setOnToastLifecycle(OnLifecycle listener) {
        mListener = listener;
        return this;
    }

    public void showAsDropDown(View anchorView) {
        showAsDropDown(anchorView, Gravity.BOTTOM);
    }

    public void showAsDropDown(View anchorView, int showGravity) {
        showAsDropDown(anchorView, showGravity, 0, 0);
    }

    /**
     * 将悬浮窗显示在某个 View 下方（和 PopupWindow 同名方法作用类似）
     *
     * @param anchorView  锚点 View
     * @param showGravity 显示重心
     * @param xOff        水平偏移
     * @param yOff        垂直偏移
     */
    @SuppressLint("ObsoleteSdkInt")
    public void showAsDropDown(View anchorView, int showGravity, int xOff, int yOff) {
        if (mDecorView.getChildCount() == 0 || mWindowParams == null) {
            throw new IllegalArgumentException("WindowParams and view cannot be empty");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // 适配布局反方向
            showGravity = Gravity.getAbsoluteGravity(showGravity,
                    anchorView.getResources().getConfiguration().getLayoutDirection());
        }

        int[] anchorViewLocation = new int[2];
        anchorView.getLocationOnScreen(anchorViewLocation);

        Rect windowVisibleRect = new Rect();
        anchorView.getWindowVisibleDisplayFrame(windowVisibleRect);

        mWindowParams.gravity = Gravity.TOP | Gravity.START;
        mWindowParams.x = anchorViewLocation[0] - windowVisibleRect.left + xOff;
        mWindowParams.y = anchorViewLocation[1] - windowVisibleRect.top + yOff;

        if ((showGravity & Gravity.LEFT) == Gravity.LEFT) {
            int rootViewWidth = mDecorView.getWidth();
            if (rootViewWidth == 0) {
                rootViewWidth = mDecorView.getMeasuredWidth();
            }
            if (rootViewWidth == 0) {
                mDecorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                rootViewWidth = mDecorView.getMeasuredWidth();
            }
            mWindowParams.x -= rootViewWidth;
        } else if ((showGravity & Gravity.RIGHT) == Gravity.RIGHT) {
            mWindowParams.x += anchorView.getWidth();
        }


        if ((showGravity & Gravity.TOP) == Gravity.TOP) {
            int rootViewHeight = mDecorView.getHeight();
            if (rootViewHeight == 0) {
                rootViewHeight = mDecorView.getMeasuredHeight();
            }
            if (rootViewHeight == 0) {
                mDecorView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                rootViewHeight = mDecorView.getMeasuredHeight();
            }
            mWindowParams.y -= rootViewHeight;
        } else if ((showGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            mWindowParams.y += anchorView.getHeight();
        }

        show();
    }

    /**
     * 显示悬浮窗
     */
    @SuppressLint("ObsoleteSdkInt")
    public void show() {
        if (mDecorView.getChildCount() == 0 || mWindowParams == null) {
            throw new IllegalArgumentException("WindowParams and view cannot be empty");
        }
        // 如果当前已经显示则进行更新
        if (mShowing) {
            update();
            return;
        }
        if (mContext instanceof Activity) {
            if (((Activity) mContext).isFinishing() ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                            ((Activity) mContext).isDestroyed())) {
                return;
            }
        }
        try {
            // 如果 View 已经被添加的情况下，就先把 View 移除掉
            if (mDecorView.getParent() != null) {
                mWindowManager.removeViewImmediate(mDecorView);
            }
            mWindowManager.addView(mDecorView, mWindowParams);
            configAnimation(mDecorView);
            // 当前已经显示
            mShowing = true;
            // 如果当前限定了显示时长
            if (mDuration != 0) {
                removeCallbacks(this);
                postDelayed(this, mDuration);
            }
            // 如果设置了拖拽规则
            if (mDraggable != null) {
                mDraggable.start(this);
            }
            // 注册 Activity 生命周期
            if (mLifecycle != null) {
                mLifecycle.register();
            }
            // 回调监听
            if (mListener != null) {
                mListener.onShow(this);
            }

        } catch (NullPointerException | IllegalStateException |
                IllegalArgumentException | WindowManager.BadTokenException e) {
            // 如果这个 View 对象被重复添加到 WindowManager 则会抛出异常
            // java.lang.IllegalStateException: View has already been added to the window manager.
            e.printStackTrace();
        }
    }

    public void configAnimation(ViewGroup mDecorView) {
        //mDecorView.setAlpha(0);
        //mDecorView.animate().alpha(1.0f).setDuration(3000).start();
    }

    /**
     * 销毁悬浮窗
     */
    public void cancel() {
        if (!mShowing) {
            return;
        }
        try {
            // 反注册 Activity 生命周期
            if (mLifecycle != null) {
                mLifecycle.unregister();
            }
            // 如果当前 WindowManager 没有附加这个 View 则会抛出异常
            // java.lang.IllegalArgumentException: View not attached to window manager
            mWindowManager.removeViewImmediate(mDecorView);
            // 移除销毁任务
            removeCallbacks(this);
            // 回调监听
            if (mListener != null) {
                mListener.onDismiss(this);
            }
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            // 当前没有显示
            mShowing = false;
        }
    }

    /**
     * 刷新悬浮窗
     */
    public void update() {
        if (!isShowing()) {
            return;
        }
        //更新WindowManger的显示
        mWindowManager.updateViewLayout(mDecorView, mWindowParams);
    }

    /**
     * 回收释放
     */
    public void recycle() {
        if (isShowing()) {
            cancel();
        }
        if (mListener != null) {
            mListener.onRecycler(this);
        }
        mListener = null;
        mContext = null;
        mDecorView = null;
        mWindowManager = null;
        mWindowParams = null;
        mLifecycle = null;
        mDraggable = null;
    }

    /**
     * 当前是否已经显示
     */
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * 获取 WindowManager 对象
     */
    public WindowManager getWindowManager() {
        return mWindowManager;
    }

    /**
     * 获取 WindowManager 参数集
     */
    public WindowManager.LayoutParams getWindowParams() {
        return mWindowParams;
    }

    /**
     * 重新设置 WindowManager 参数集
     */
    public FloatingView setWindowParams(WindowManager.LayoutParams params) {
        mWindowParams = params;
        update();
        return this;
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 获取根布局
     */
    public View getDecorView() {
        return mDecorView;
    }

    /**
     * 设置根布局（一般情况下推荐使用 {@link #setContentView} 方法来填充布局）
     */
    public FloatingView setDecorView(ViewGroup viewGroup) {
        mDecorView = viewGroup;
        return this;
    }

    /**
     * 获取内容布局
     */
    public View getContentView() {
        if (mDecorView.getChildCount() == 0) {
            return null;
        }
        return mDecorView.getChildAt(0);
    }

    /**
     * 设置内容布局
     */
    public FloatingView setContentView(int id) {
        return setContentView(LayoutInflater.from(mContext).inflate(id, mDecorView, false));
    }

    public FloatingView setContentView(View view) {
        if (mDecorView.getChildCount() > 0) {
            mDecorView.removeAllViews();
        }
        mDecorView.addView(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = ((ViewGroup.MarginLayoutParams) layoutParams);
            // 清除 Margin，因为 WindowManager 没有这一属性可以设置，并且会跟根布局相冲突
            marginLayoutParams.topMargin = 0;
            marginLayoutParams.bottomMargin = 0;
            marginLayoutParams.leftMargin = 0;
            marginLayoutParams.rightMargin = 0;
        }
        // 如果当前没有设置重心，就自动获取布局重心
        if (mWindowParams.gravity == Gravity.NO_GRAVITY) {
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                int gravity = ((FrameLayout.LayoutParams) layoutParams).gravity;
                if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                    mWindowParams.gravity = gravity;
                }
            } else if (layoutParams instanceof LinearLayout.LayoutParams) {
                int gravity = ((LinearLayout.LayoutParams) layoutParams).gravity;
                if (gravity != FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY) {
                    mWindowParams.gravity = gravity;
                }
            }
            if (mWindowParams.gravity == Gravity.NO_GRAVITY) {
                // 默认重心是居中
                mWindowParams.gravity = Gravity.CENTER;
            }
        }
        if (layoutParams != null) {
            if (mWindowParams.width == WindowManager.LayoutParams.WRAP_CONTENT &&
                    mWindowParams.height == WindowManager.LayoutParams.WRAP_CONTENT) {
                // 如果当前 Dialog 的宽高设置了自适应，就以布局中设置的宽高为主
                mWindowParams.width = layoutParams.width;
                mWindowParams.height = layoutParams.height;
            } else {
                // 如果当前通过代码动态设置了宽高，则以动态设置的为主
                layoutParams.width = mWindowParams.width;
                layoutParams.height = mWindowParams.height;
            }
        }
        update();
        return this;
    }

    /**
     * 根据 ViewId 获取 View
     */
    public <V extends View> V findViewById(int id) {
        return mDecorView.findViewById(id);
    }

    /**
     * 跳转 Activity
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(mContext, clazz));
    }

    public void startActivity(Intent intent) {
        if (!(mContext instanceof Activity)) {
            //如果当前的上下文不是Activity调用，startActivity必须加入新任务栈的标记
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    /**
     * 设置可见状态
     */
    public FloatingView setVisibility(int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    /**
     * 设置文本
     */
    public FloatingView setText(int id) {
        return setText(android.R.id.message, id);
    }

    public FloatingView setText(int viewId, int stringId) {
        return setText(viewId, mContext.getResources().getString(stringId));
    }

    public FloatingView setText(CharSequence text) {
        return setText(android.R.id.message, text);
    }

    public FloatingView setText(int id, CharSequence text) {
        ((TextView) findViewById(id)).setText(text);
        return this;
    }

    /**
     * 设置文本颜色
     */
    public FloatingView setTextColor(int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
        return this;
    }

    /**
     * 设置提示
     */
    public FloatingView setHint(int viewId, int stringId) {
        return setHint(viewId, mContext.getResources().getString(stringId));
    }

    public FloatingView setHint(int id, CharSequence text) {
        ((TextView) findViewById(id)).setHint(text);
        return this;
    }

    /**
     * 设置提示文本颜色
     */
    public FloatingView setHintColor(int id, int color) {
        ((TextView) findViewById(id)).setHintTextColor(color);
        return this;
    }

    /**
     * 设置背景
     */
    @SuppressLint("ObsoleteSdkInt")
    public FloatingView setBackground(int viewId, int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(mContext, drawableId);
        return setBackground(viewId, drawable);
    }

    @SuppressLint("ObsoleteSdkInt")
    public FloatingView setBackground(int id, Drawable drawable) {
        ViewCompat.setBackground(findViewById(id), drawable);
        return this;
    }

    /**
     * 设置图片
     */
    public FloatingView setImageDrawable(int viewId, int drawableId) {
        Drawable drawable = AppCompatResources.getDrawable(mContext, drawableId);
        return setImageDrawable(viewId, drawable);
    }

    public FloatingView setImageDrawable(int viewId, Drawable drawable) {
        ((ImageView) findViewById(viewId)).setImageDrawable(drawable);
        return this;
    }

    /**
     * 获取 Handler
     */
    public Handler getHandler() {
        return HANDLER;
    }

    /**
     * 延迟执行
     */
    public boolean post(Runnable runnable) {
        return postDelayed(runnable, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public boolean postDelayed(Runnable runnable, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(runnable, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public boolean postAtTime(Runnable runnable, long uptimeMillis) {
        //发送和这个WindowManager相关的消息回调
        return HANDLER.postAtTime(runnable, this, uptimeMillis);
    }

    /**
     * 移除消息回调
     */
    public void removeCallbacks(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }

    public void removeCallbacksAndMessages() {
        HANDLER.removeCallbacksAndMessages(this);
    }

    /**
     * 设置点击事件
     */
    public FloatingView setOnClickListener(OnClickListener listener) {
        return setOnClickListener(mDecorView, listener);
    }

    public FloatingView setOnClickListener(int id, OnClickListener listener) {
        return setOnClickListener(findViewById(id), listener);
    }

    private FloatingView setOnClickListener(View view, OnClickListener listener) {
        // 如果当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        view.setClickable(true);
        view.setOnClickListener(new ViewClickWrapper(this, listener));
        return this;
    }

    /**
     * 设置长按事件
     */
    public FloatingView setOnLongClickListener(OnLongClickListener<? extends View> listener) {
        return setOnLongClickListener(mDecorView, listener);
    }

    public FloatingView setOnLongClickListener(int id, OnLongClickListener<? extends View> listener) {
        return setOnLongClickListener(findViewById(id), listener);
    }

    private FloatingView setOnLongClickListener(View view, OnLongClickListener<? extends View> listener) {
        //如果当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        view.setClickable(true);
        view.setOnLongClickListener(new ViewLongClickWrapper(this, listener));
        return this;
    }

    /**
     * 设置触摸事件
     */
    public FloatingView setOnTouchListener(OnTouchListener<? extends View> listener) {
        return setOnTouchListener(mDecorView, listener);
    }

    public FloatingView setOnTouchListener(int id, OnTouchListener<? extends View> listener) {
        return setOnTouchListener(findViewById(id), listener);
    }

    private FloatingView setOnTouchListener(View view, OnTouchListener<? extends View> listener) {
        // 当前是否设置了不可触摸，如果是就擦除掉
        clearWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        view.setEnabled(true);
        view.setOnTouchListener(new ViewTouchWrapper(this, listener));
        return this;
    }

    /**
     * {@link Runnable}
     */
    @Override
    public void run() {
        cancel();
    }

    /**
     * View 的点击事件监听
     */
    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick(FloatingView floatingView, View view);
    }

    /**
     * View的长按事件监听
     */
    public interface OnLongClickListener<V extends View> {
        /**
         * 长按回调
         */
        boolean onLongClick(FloatingView toast, V view);
    }

    /**
     * View的触摸事件监听
     */
    public interface OnTouchListener<V extends View> {
        /**
         * 触摸回调
         */
        boolean onTouch(FloatingView toast, V view, MotionEvent event);
    }

    /**
     * Toast 生命周期监听
     */
    public interface OnLifecycle {
        /**
         * 显示回调
         */
        default void onShow(FloatingView toast) {
        }

        /**
         * 消失回调
         */
        default void onDismiss(FloatingView toast) {
        }

        /**
         * 回收回调
         */
        default void onRecycler(FloatingView toast) {
        }
    }
}