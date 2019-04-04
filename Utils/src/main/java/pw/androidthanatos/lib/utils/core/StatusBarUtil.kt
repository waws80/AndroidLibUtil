package pw.androidthanatos.lib.utils.core

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import pw.androidthanatos.lib.utils.LibUtils

/**
 *  @desc: 状态栏工具类
 *  @className: StatusBarUtil.kt
 *  @author: thanatos
 *  @createTime: 2019/1/29
 *  @updateTime: 2019/1/29 16:05
 */
class StatusBarUtil private constructor(private val application: Application){

    companion object {

        val invoke = StatusBarUtil(LibUtils.getApplication())
    }


    @JvmOverloads
    fun setImmerseLayout(activity: Activity, @ColorInt color: Int = Color.TRANSPARENT, light: Boolean = false){
        setImmerseLayout(activity.window, color, light)
    }

    @JvmOverloads
    fun setImmerseLayout(window: Window, @ColorInt color: Int = Color.TRANSPARENT, light: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && light){
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            }else{
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            }

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT

            val vg = window.findViewById<ViewGroup>(android.R.id.content)
            //状态栏
            val statusBarView = View(window.context)
            statusBarView.setBackgroundColor(color)
            //半透明效果
            val markerView = View(window.context)
            markerView.setBackgroundColor(Color.argb(60,0,0,0))
            vg.addView(statusBarView, ViewGroup.LayoutParams(-1, getStatusBarHeight()))
            vg.addView(markerView, ViewGroup.LayoutParams(-1, getStatusBarHeight()))
        }
    }

    /**
     * 获取状态栏高度
     * @return 状态栏高度
     */
    fun getStatusBarHeight(): Int {
        // 获得状态栏高度
        val resourceId = application.resources.getIdentifier("status_bar_height", "dimen", "android")
        return application.resources.getDimensionPixelSize(resourceId)
    }
}