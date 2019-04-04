package pw.androidthanatos.lib.utils.core

import android.annotation.SuppressLint
import android.os.Build
import android.text.TextUtils


/**
 * @desc: rom工具类
 * @className: RomUtil
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午4:18
 */
class RomUtil {


    companion object {

        private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
        private const val KEY_EMUI_VERSION_NAME = "ro.build.version.emui"

        val invoke = RomUtil()
    }




    /**
     * 判断是否为MIUI
     *
     * @return
     */
    val isMIUI = !TextUtils.isEmpty(getSystemProperty(KEY_MIUI_VERSION_NAME, ""))

    /**
     * 获取MUI版本
     * @return
     */
    val miuiVersion = if (isMIUI) getSystemProperty(KEY_MIUI_VERSION_NAME, "") else ""

    /**
     * 获取MIUI版本-数字用于大小判断
     * @return
     */
    val miuiVersionCode: Int
        get() {
            var code = -1
            var property = miuiVersion
            try {
                property = property.trim { it <= ' ' }.toUpperCase().replace("V", "")
                code = Integer.parseInt(property)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return code
        }

    /**
     * 判断是否为EMUI
     * @return
     */
    val isEMUI = !TextUtils.isEmpty(getSystemProperty(KEY_EMUI_VERSION_NAME, ""))


    /**
     * 获取EMUI的版本
     *
     * @return
     */
    val emuiVersion = if (isEMUI) getSystemProperty(KEY_EMUI_VERSION_NAME, "") else ""

    /**
     * 判断是否为Flyme
     * @return
     */
    val isFlyme = Build.DISPLAY.toLowerCase().contains("flyme")

    /**
     * 获取Flyme的版本
     *
     * @return
     */
    val flymeVersion = (if (isFlyme) Build.DISPLAY else "")!!

    /**
     * 获取Flyme版本号
     *
     * @return
     */
    val flymeVersionCode: Int
        get() {
            var code = 0
            val version = flymeVersion
            if (!TextUtils.isEmpty(version)) {
                code = if (version.toLowerCase().contains("os")) {
                    Integer.valueOf(version.substring(9, 10))
                } else {
                    Integer.valueOf(version.substring(6, 7))
                }
            }
            return code
        }


    /**
     * 通过反射获取系统属性
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @SuppressLint("PrivateApi")
    fun getSystemProperty(key: String, defaultValue: String): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val get = clz.getMethod("get", String::class.java, String::class.java)
            return get.invoke(clz, key, defaultValue) as String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defaultValue
    }

}
