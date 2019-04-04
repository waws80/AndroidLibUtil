package pw.androidthanatos.lib.utils.network

import java.lang.reflect.Method

/**
 * 方法信息类
 */
data class MethodBean(val method: Method, //方法
                      val threadType: ThreadType,//运行的线程类型
                      val receiverType: NetWorkType)//接收的网络类型