package pw.androidthanatos.lib.utils.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import pw.androidthanatos.lib.utils.LibUtils

/**
 * 功能描述: 网络状态工具类
 * @className: NetWorkCallBackImpl.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午4:21
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NetWorkCallBackImpl private constructor() : ConnectivityManager.NetworkCallback() {


    companion object {

        private const val NETWORK_NONE=-1 //无网络连接
        private const val NETWORK_WIFI=0 //wifi
        private const val NETWORK_MOBILE=1 //数据网络

        //上一次网络状态
        private var lastStatus = -2

        private var sNetWorkCallback: (NetWorkType) ->Unit = {}

        val instance = NetWorkCallBackImpl()

        fun setNetWorkCallBack(netWorkCallback: (NetWorkType) ->Unit){
            sNetWorkCallback = netWorkCallback
        }

    }

    override fun onAvailable(network: Network?) {
        super.onAvailable(network)
        if (NetUtil.invoke.isWifi()){
            if (lastStatus != NETWORK_WIFI){
                LibUtils.logUtil().d("网络为WIFI")
                sNetWorkCallback.invoke(NetWorkType.WIFI)
                lastStatus = NETWORK_WIFI
            }
        }else{
            if (lastStatus != NETWORK_MOBILE){
                LibUtils.logUtil().d("网络类型为流量")
                sNetWorkCallback.invoke(NetWorkType.MOBILE)
                lastStatus = NETWORK_MOBILE
            }
        }
    }

    override fun onLost(network: Network?) {
        super.onLost(network)
        if (lastStatus != NETWORK_NONE){
            LibUtils.logUtil().d("网络断开连接")
            sNetWorkCallback.invoke(NetWorkType.NONE)
            lastStatus = NETWORK_NONE
        }
    }

    override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
        super.onCapabilitiesChanged(network, networkCapabilities)
    }


}