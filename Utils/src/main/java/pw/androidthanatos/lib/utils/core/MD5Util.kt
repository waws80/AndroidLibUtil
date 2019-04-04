package pw.androidthanatos.lib.utils.core

import java.security.MessageDigest

/**
 * 功能描述: md5工具类
 * @className: MD5Util.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午2:20
 */
class MD5Util {

    companion object {

        private val hexDigIts = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")


        val invoke = MD5Util()
    }

    /**
     * MD5加密
     * @param str 字符
     * @return
     */
    fun md5(str: String): String? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            byteArrayToHexString(md.digest(str.toByteArray()))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }


    private fun byteArrayToHexString(b: ByteArray): String {
        val resultSb = StringBuffer()
        for (i in b.indices) {
            resultSb.append(byteToHexString(b[i]))
        }
        return resultSb.toString()
    }

    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigIts[d1] + hexDigIts[d2]
    }

}