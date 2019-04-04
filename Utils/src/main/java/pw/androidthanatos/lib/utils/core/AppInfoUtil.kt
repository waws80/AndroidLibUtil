package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import pw.androidthanatos.lib.utils.LibUtils

/**
 * 功能描述: app基本信息工具类
 * @className: AppInfoUtil.kt
 * @author: thanatos
 * @createTime: 19-4-4
 * @updateTime: 19-4-4 上午10:41
 */
class AppInfoUtil private constructor(private val application: Application){

    companion object {

        val invoke = AppInfoUtil(LibUtils.getApplication())
    }


    /**
     * 获取版本号
     */
    fun getVersionCode(): Long {
        val info = application.packageManager.getPackageInfo(application.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            info.longVersionCode
        }else{
            info.versionCode.toLong()
        }
    }


    /**
     * 获取版本名字
     */
    fun getVersionName() = application.packageManager.getPackageInfo(application.packageName, 0).versionName


    /**
     * 获取名字
     */
    fun getName() = application.packageManager.getApplicationLabel(application.packageManager.
        getPackageInfo(application.packageName, 0).applicationInfo)


    /**
     * 获取包名
     */
    fun getPackageName() = application.packageName

    /**
     * 获取图标
     */
    fun getIcon() = application.packageManager.getApplicationIcon(application.packageManager.
        getPackageInfo(application.packageName, 0).applicationInfo)

    /**
     * 获取第三方包信息
     */
    fun apkInfo(apkPath: String): PackageInfo {
       return application.packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)
    }

    /**
     * 获取第三方版本号
     */
    fun getApkVersionCode(apkPath: String): Long{
        val info = apkInfo(apkPath)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            info.longVersionCode
        }else{
            info.versionCode.toLong()
        }
    }


    /**
     * 获取第三方版本名称
     */
    fun getApkVersionName(apkPath: String): String{
        val info = apkInfo(apkPath)
        return info.versionName
    }

    /**
     * 获取第三方名称
     */
    fun getApkName(apkPath: String): String{
        val appInfo = apkInfo(apkPath).applicationInfo
        appInfo.sourceDir = apkPath
        appInfo.publicSourceDir = apkPath
        return application.packageManager.getApplicationLabel(appInfo).toString()
    }

    /**
     * 获取第三方包名
     */
    fun getApkPackageName(apkPath: String): String{
        val appInfo = apkInfo(apkPath).applicationInfo
        appInfo.sourceDir = apkPath
        appInfo.publicSourceDir = apkPath
        return appInfo.packageName
    }

    /**
     * 获取第三方icon
     */
    fun getApkIcon(apkPath: String): Drawable{
        val appInfo = apkInfo(apkPath).applicationInfo
        appInfo.sourceDir = apkPath
        appInfo.publicSourceDir = apkPath
        val icon = application.packageManager.getApplicationIcon(appInfo)
        val icon1 = appInfo.loadIcon(application.packageManager)
        return icon ?: icon1
    }


}