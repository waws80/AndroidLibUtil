package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.Intent
import android.os.Looper
import pw.androidthanatos.lib.utils.LibUtils


/**
 * @desc: 全局异常捕获类
 * @className: CustomCrashHandler
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午4:03
 */
class CustomCrashHandler private constructor(private val application: Application,
                                             private val toastUtil: ToastUtil): Thread.UncaughtExceptionHandler {


    companion object {

        private var message = "应用出现异常，即将重启"
        private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        private val instance = CustomCrashHandler(LibUtils.getApplication(),
            LibUtils.toast())

        private var sCrashCall:(Throwable)->Unit = {}

        /**
         * 初始化
         */
        @JvmOverloads
        @JvmStatic
        fun init(message: String = "应用出现异常，即将重启", crashCallBack: (Throwable) ->Unit = {}) {
            Companion.message = message
            Thread.setDefaultUncaughtExceptionHandler(instance)
            sCrashCall = crashCallBack
        }
    }



    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val res = handleException(throwable)
        if (!res) {
            defaultHandler.uncaughtException(thread, throwable)
        } else {
            try {
                Thread.sleep(1500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                //错误信息
                sCrashCall.invoke(e)
            }finally {
                System.gc()
                restart()
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }


    private fun restart() {
        val intent = application.packageManager.getLaunchIntentForPackage(application.packageName)
        if (intent != null) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            application.startActivity(intent)
        }

    }

    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        object : Thread() {
            override fun run() {
                Looper.prepare()
                ex.printStackTrace()
                toastUtil.show(message)
                Looper.loop()
            }
        }.start()
        return true
    }
}
