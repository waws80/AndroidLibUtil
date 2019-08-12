package pw.androidthanatos.lib.utils.network

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.annotation.RequiresApi
import pw.androidthanatos.lib.utils.LibUtils
import java.lang.reflect.Method


/**
 * @desc: 网络连接工具类
 * @className: NetUtil
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午3:51
 */
class NetUtil private constructor(private val application: Application){

    companion object {

        private val netChangeListenerMap = HashMap<Any,List<MethodBean>>()

        val invoke = NetUtil(LibUtils.getApplication())

    }

    private val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val netReceiver = NetReceiver {
        sendNetWorkType(it)
    }


    /**
     * 初始化
     */
    fun init(){

        addActivityLifeCallback()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            NetWorkCallBackImpl.setNetWorkCallBack {
                sendNetWorkType(it)
            }
            manager.registerNetworkCallback(NetworkRequest.Builder().build(), NetWorkCallBackImpl.instance)
        }else{
            application.registerReceiver(netReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }


    /**
     * 注销
     */
    fun destroy(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            manager.unregisterNetworkCallback(NetWorkCallBackImpl.instance)
        }else{
            application.unregisterReceiver(netReceiver)
        }
    }




    /**
     * 判断网络是否连接
     * @return
     */
    fun isConnected(): Boolean {
        val connectivity = application
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (null != connectivity) {
            val info = connectivity.activeNetworkInfo
            if (null != info) {
                return info.isConnected
            }
        }
        return false
    }

    /**
     * 判断是否是wifi连接
     */
    fun isWifi(): Boolean {
        val cm = application
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.type == ConnectivityManager.TYPE_WIFI
    }




    /**
     * 添加activity生命周期监听事件
     */
    private fun addActivityLifeCallback(){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                bind(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                unBind(activity)
            }

        })
    }

    /**
     * 解绑
     */
    fun unBind(obj: Any){
        if (obj == null){
            return
        }
        netChangeListenerMap.remove(obj)
    }

    /**
     * 组合当前页面符合通知网络类型的方法
     */
    fun bind(obj: Any) {
        if (obj == null){
            return
        }
        val methods = obj::class.java.methods
        val methodBeanList = mutableListOf<MethodBean>()
        methods.forEach {method ->
            val subscribeNetWork = method.getAnnotation(SubscribeNetWork::class.java)
            if (subscribeNetWork != null){
                if (method.returnType.name == "void"){
                    if (method.parameterTypes.size == 1
                        && method.parameterTypes[0].name == "pw.androidthanatos.lib.utils.network.NetWorkType"){
                        LibUtils.logUtil().d(tag = "NetUtil", msg = "目标类：${activity::class.java.name}, 方法：${method.name}被添加入网络监听列表，接收的网络类型：${subscribeNetWork.receiveType}, 接收的线程类型：${subscribeNetWork.value}")
                        methodBeanList.add(MethodBean(method,subscribeNetWork.value, subscribeNetWork.receiveType))
                    }else{
                        LibUtils.logUtil().e(tag = "NetUtil", msg = "${method.name}的参数只能有一个，并且是 NetWorkType")
                    }
                }else{
                    LibUtils.logUtil().e(tag = "NetUtil", msg = "${method.name}的返回值不是 void")
                }
            }
        }
        netChangeListenerMap[obj] = methodBeanList
    }

    /**
     * 批量发送网络状态
     */
    private fun sendNetWorkType(netWorkType: NetWorkType){
        netChangeListenerMap.forEach {
            sendNetWorkTypeToTarget(it.key, it.value, netWorkType)
        }
    }

    /**
     * 给目标页面合适的方法发送网络状态信息
     */
    private fun sendNetWorkTypeToTarget(obj: Any, list: List<MethodBean>, netWorkType: NetWorkType) {
        if (obj is Activity && (obj as Activity).isFinishing) return
        list.forEach {
            sendNetWorkTypeToMethod(obj, it, netWorkType)
        }
    }

    /**
     * 分发网络状态信息给目标
     */
    private fun sendNetWorkTypeToMethod(obj: Any, bean: MethodBean, netWorkType: NetWorkType) {
        if (obj is Activity && (obj as Activity).isFinishing) return
        if (bean.receiverType == netWorkType || bean.receiverType == NetWorkType.ALL){
            sendToMethod(obj, bean.method, bean.threadType, netWorkType)
        }
    }


    private fun sendToMethod(obj: Any, method: Method, threadType: ThreadType, netWorkType: NetWorkType) {
        when(threadType){
            ThreadType.POSTING -> {
                send(obj, method, netWorkType)
            }
            ThreadType.MAIN -> {
                if (Looper.getMainLooper() != Looper.myLooper()){
                    LibUtils.weakHandler().post { send(obj, method, netWorkType) }
                }else{
                    send(obj, method, netWorkType)
                }
            }
            ThreadType.BACKGROUND -> {
                LibUtils.threadPool().execute {
                    send(obj, method, netWorkType)
                }
            }
        }
    }

    /**
     * 调用方法
     */
    private fun send(obj: Any, method: Method, netWorkType: NetWorkType){
        method.invoke(obj, netWorkType)
    }


}


/**
 * 网络状态接收器
 */
private class NetReceiver(private val netWorkCallback:(NetWorkType)->Unit): BroadcastReceiver(){

    companion object {

        private const val NETWORK_NONE=-1 //无网络连接
        private const val NETWORK_WIFI=0 //wifi
        private const val NETWORK_MOBILE=1 //数据网络

        //上一次网络状态
        private var lastStatus = -2
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if(intent?.action == ConnectivityManager.CONNECTIVITY_ACTION){
            when(getNetWorkState(context)){
                NETWORK_NONE -> {
                    if (lastStatus != NETWORK_NONE){
                        lastStatus = NETWORK_NONE
                        netWorkCallback.invoke(NetWorkType.NONE)
                    }
                }
                NETWORK_WIFI -> {
                    if (lastStatus != NETWORK_WIFI){
                        lastStatus = NETWORK_WIFI
                        netWorkCallback.invoke(NetWorkType.WIFI)
                    }
                }
                NETWORK_MOBILE -> {
                    if (lastStatus != NETWORK_MOBILE){
                        lastStatus = NETWORK_MOBILE
                        netWorkCallback.invoke(NetWorkType.MOBILE)
                    }
                }
            }

        }
    }

    /**
     * 网络状态
     */
   private fun getNetWorkState(context: Context): Int{
        val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected){
            if(activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI){
                return NETWORK_WIFI
            }else if(activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE){
                return NETWORK_MOBILE
            }
        }else{
            return NETWORK_NONE
        }
        return NETWORK_NONE
    }
}

