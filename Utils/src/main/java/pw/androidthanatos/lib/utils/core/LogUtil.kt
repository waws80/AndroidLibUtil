package pw.androidthanatos.lib.utils.core

import android.util.Log
import pw.androidthanatos.lib.utils.LibUtils

/**
 * 日志工具类
 */
class LogUtil private constructor(private val debug: Boolean){

    companion object {

        val invoke = LogUtil(LibUtils.isDebug())

    }

    fun d(msg: String, tag: String = "LogUtil"){
        if (debug){
            Log.d(tag, msg)
        }
    }

    fun w(msg: String, tag: String = "LogUtil"){
        if (debug){
            Log.w(tag, msg)
        }
    }

    fun e(msg: String, tag: String = "LogUtil"){
        if (debug){
            Log.e(tag, msg)
        }
    }
}