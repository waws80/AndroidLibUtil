package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.view.Gravity
import android.view.View
import android.widget.Toast
import pw.androidthanatos.lib.utils.LibUtils

/**
 * @desc: [Toast] 工具类
 * @className: ToastUtil
 * @author: thanatos
 * @createTime: 2018/10/18
 * @updateTime: 2018/10/18 上午10:43
 */
class ToastUtil private constructor(private val application: Application){


    companion object {
        val invoke = ToastUtil(LibUtils.getApplication())
    }


    /**
     * 显示普通toast
     * @param s 文本
     * @param duration 时长
     */
    fun show(s: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        if (s.isNullOrEmpty()) return
        Toast.makeText(application, s, duration).show()
    }

    /**
     * 显示居中toast
     * @param s 文本
     * @param duration 时长
     */
    fun center(s: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
        if (s.isNullOrEmpty()) return
        Toast.makeText(application, s, duration)
            .apply {
                this.setGravity(Gravity.CENTER, 0, 0)
                this.show()
            }
    }

    /**
     * 显示自定义toast
     * @param view 自定义布局
     * @param duration 时长
     * @param gravity 位置
     */
    fun custom(view: View, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.BOTTOM,
               xOffset: Int = 0, yOffset: Int = 0) {
        Toast(application).apply {
            this.duration = duration
            this.setGravity(gravity, xOffset, yOffset)
            this.view = view
            this.show()
        }
    }

}

