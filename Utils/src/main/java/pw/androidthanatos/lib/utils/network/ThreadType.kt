package pw.androidthanatos.lib.utils.network

enum class ThreadType {

    MAIN, //在主线程接收
    BACKGROUND, //在子线程接收
    POSTING// 在默认线程
}