package pw.androidthanatos.android.libutil

import android.app.Application
import pw.androidthanatos.lib.utils.LibUtils
import pw.androidthanatos.lib.utils.network.NetUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //初始化工具类
        LibUtils.install(this, true)
    }


    override fun onLowMemory() {
        super.onLowMemory()
        //注销
        LibUtils.destroy()
    }
}