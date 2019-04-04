package pw.androidthanatos.lib.utils.core

import android.app.Activity
import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import pw.androidthanatos.lib.utils.LibUtils


/**
 * @desc: 屏幕工具类
 * @className: ScreenUtils
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午4:27
 */
class ScreenUtil private constructor(private val application: Application){

    companion object {

        val invoke = ScreenUtil(LibUtils.getApplication())
    }

    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = application.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = application.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    /**
     * 获取标题栏高度
     *
     * @param activity
     * @return
     */
    fun getTitleBarHeight(activity: Activity): Int {
        val contentTop = activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        return contentTop - statusBarHeight
    }

    /**
     * 在Activity中获取屏幕的高度和宽度
     *
     * @param activity 在真机中，有时候会发现得到的尺寸不是很准确，需要在AndroidManifest中添加如下配置：
     * <supports-screens android:smallScreens="true" android:normalScreens="true" android:largeScreens="true" android:resizeable="true" android:anyDensity="true"></supports-screens>
     */
    fun getScreenSize(activity: Activity): IntArray {
        val display = activity.windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        return intArrayOf(point.x, point.y)
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     *
     * @param context
     * @return
     */
    fun getScreenMetrics(): Point {
        val dm = application.resources.displayMetrics
        val w_screen = dm.widthPixels
        val h_screen = dm.heightPixels
        return Point(w_screen, h_screen)

    }

    /**
     * 获取屏幕长宽比
     *
     * @param context
     * @return 比例值
     */
    fun getScreenRate(): Float {
        val P = getScreenMetrics()
        val H = P.y.toFloat()
        val W = P.x.toFloat()
        return H / W
    }

    /**
     * 在非Activity中，通常会在Custom View时
     * 目前不推荐使用（3.2及以下）
     *
     * 在真机中，有时候会发现得到的尺寸不是很准确，需要在AndroidManifest中添加如下配置：
     * <supports-screens android:smallScreens="true" android:normalScreens="true" android:largeScreens="true" android:resizeable="true" android:anyDensity="true"></supports-screens>
     */
    fun getScreenSize(): IntArray {
        val wm = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return intArrayOf(dm.widthPixels, dm.heightPixels)
    }


    /**
     * 是否锁屏
     * 实时获取状态@see <p>https://blog.csdn.net/fantasy_lin_/article/details/83276325</p>
     * @return
     */
    fun isScreenLocked(): Boolean{
        val manager = application.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            manager.isKeyguardLocked
        } else {
            manager.inKeyguardRestrictedInputMode()
        }
    }


    /**
     * dp转px
     * @param dp
     * @return
     */
    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, application.resources.displayMetrics).toInt()
    }

    /**
     * sp转px
     * @param sp
     * @return
     */
    fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, application.resources.displayMetrics).toInt()
    }


    /**
     * px转dp
     */
    fun px2dp(px: Int): Int {
        val scale = application.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    fun px2sp(px: Int): Int {
        val scale = application.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }


}
