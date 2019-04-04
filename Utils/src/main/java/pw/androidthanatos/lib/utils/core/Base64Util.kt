package pw.androidthanatos.lib.utils.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

/**
 * 功能描述: Base64工具类
 * @className: Base64Util.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午2:22
 */
class Base64Util private constructor() {

    companion object {

        val invoke = Base64Util()
    }

    /**
     * base64加密
     * @param str
     * @return
     */
    fun encodeBase64(str: String): String {
        return String(Base64.encode(str.toByteArray(), Base64.DEFAULT))
    }

    /**
     * base64解密
     * @param str
     * @return
     */
    fun decodeBase64(str: String): String {
        return String(Base64.decode(str, Base64.DEFAULT))
    }

    /**
     * base64转bitmap
     * @param base64
     * @return
     */
    fun base64ToBitmap(base64: String): Bitmap? {
        val bytes = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

}