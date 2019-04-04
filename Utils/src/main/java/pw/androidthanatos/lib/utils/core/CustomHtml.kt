package pw.androidthanatos.lib.utils.core

import android.os.Build
import android.text.Html
import android.text.Spanned

import android.text.Html.FROM_HTML_MODE_LEGACY

/**
 * 功能描述: Html 转 Spannable
 * @className: CustomHtml
 * @author: thanatos
 * @createTime: 2018/2/23
 * @updateTime: 2018/2/23 11:04
 */
class CustomHtml private constructor(){

    companion object {

        fun invoke() = CustomHtml()
    }

    /**
     * html 转 spanned
     */
    fun fromHtml(html: String, getter: Html.ImageGetter? = null): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, FROM_HTML_MODE_LEGACY, getter, CustomTagHandler())
        } else {
            Html.fromHtml(html, getter, CustomTagHandler())
        }
    }
}
