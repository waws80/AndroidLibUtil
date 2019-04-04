package pw.androidthanatos.lib.utils.network

/**
 * 网络类型注解
 */
annotation class SubscribeNetWork(val value: ThreadType = ThreadType.MAIN,//运行的线程
                                  val receiveType: NetWorkType = NetWorkType.ALL)//接收的网络类型