package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import pw.androidthanatos.lib.utils.LibUtils


/**
 * @desc: 剪切复制工具类
 * @className: ClipboardUtils
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午3:48
 */
class ClipBoardUtil private constructor(private val application: Application){


    companion object {

        val invoke = ClipBoardUtil(LibUtils.getApplication())
    }

    private var mNewClipBoardManager = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    /**
     * 获取剪切板的内容
     * @return
     */
    var text: CharSequence
        get() {
            val sb = StringBuilder()
            if (!mNewClipBoardManager.hasPrimaryClip()) {
                return sb.toString()
            } else {
                val clipData = mNewClipBoardManager.primaryClip
                val count = clipData!!.itemCount
                for (i in 0 until count) {
                    val item = clipData.getItemAt(i)
                    val str = item.coerceToText(application)
                    sb.append(str)
                }
            }
            return sb.toString()
        }
        set(value){
            val clip = ClipData.newPlainText("simple text", value)//label为simple text
            // Set the clipboard's primary clip.
            mNewClipBoardManager.primaryClip = clip
        }

    /**
     * 为剪切板设置内容
     * @param text
     */
    @Deprecated("使用 text 的set方法")
    fun copyToClipBoard(text: CharSequence) {
        // Creates a new text clip to put on the clipboard
        val clip = ClipData.newPlainText("simple text", text)//label为simple text
        // Set the clipboard's primary clip.
        mNewClipBoardManager.primaryClip = clip
    }

}
