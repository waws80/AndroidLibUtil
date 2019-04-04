package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import pw.androidthanatos.lib.utils.LibUtils


/**
 * @desc: 键盘工具类
 * @className: KeyboardUtils
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午4:19
 */
class KeyBoardUtil private constructor(private val application: Application){

    companion object {

        val invoke = KeyBoardUtil(LibUtils.getApplication())
    }

    private val sInputMethodManager = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    private val sHandler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * 输入法是否显示着
     * @return
     */
    fun isKeyBoardShow() = sInputMethodManager.isActive


    /**
     * 隐藏软键盘,上下文
     * @param view
     */
    fun hideSoftKeyboard(view: View) {
        sInputMethodManager.hideSoftInputFromWindow(view.windowToken, 0) // 强制隐藏键盘
    }

    /**
     * 隐藏虚拟键盘
     * @param v
     */
    fun hideKeyboard(v: View) {
        sInputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    /**
     * 显示虚拟键盘
     * @param v
     */
    fun showKeyboard(v: View) {
        sInputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)//表示强制显示
    }

    /**
     * 强制显示或者关闭系统键盘
     * @param txtSearchKey
     * @param show
     */
    fun keyBoard(txtSearchKey: EditText, show: Boolean) {

        sHandler.postDelayed({
            if (show) {
                sInputMethodManager.showSoftInput(txtSearchKey, InputMethodManager.SHOW_FORCED)
            } else {
                sInputMethodManager.hideSoftInputFromWindow(txtSearchKey.windowToken, 0)
            }
        }, 100L)
    }


    /**
     * 延迟隐藏虚拟键盘
     * @param v
     * @param delay
     */
    fun hideKeyboardDelay(v: View, delay: Long) {
        sHandler.postDelayed({
            sInputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }, delay)
    }

}
