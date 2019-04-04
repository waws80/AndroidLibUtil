package pw.androidthanatos.lib.utils.network

/**
 * 功能描述: 网络类型
 * @className: NetWorkType.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午4:22
 */
enum class NetWorkType {

    WIFI, //当前网络类型是wifi
    MOBILE, //当前网络类型是移动流量
    NONE, //当前暂无网络
    ALL,//所有网络类型 （主要用在注解上）
}