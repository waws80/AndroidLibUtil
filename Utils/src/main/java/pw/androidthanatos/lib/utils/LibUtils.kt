package pw.androidthanatos.lib.utils

import android.app.Application
import android.widget.Toast
import android.os.Handler
import android.webkit.WebView
import android.graphics.Bitmap
import android.os.Looper
import android.text.Html
import android.text.Spanned
import pw.androidthanatos.lib.utils.core.*
import pw.androidthanatos.lib.utils.network.NetUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @desc: 工具类
 * @className: Utils
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午3:43
 */
object LibUtils {

    @JvmStatic
    private var appContext: Application? = null

    @JvmStatic
    private var isDebug = false

    /**
     * 初始化工具类
     */
    fun install(context: Application, debug: Boolean){
        appContext = context
        isDebug = debug
        NetUtil.invoke.init()
    }

    /**
     * 销毁
     */
    fun destroy(){
        NetUtil.invoke.destroy()
    }



    /**
     * 获取程序上下文对象
     */
    fun getApplication(): Application{
        checkContext()
        return appContext!!
    }


    /**
     * 判断是否是调试模式
     */
    fun isDebug(): Boolean{
        checkContext()
        return isDebug
    }


    fun appInfo(): AppInfoUtil{
        checkContext()
        return AppInfoUtil.invoke
    }


    /**
     * base64工具类
     */
    fun base64Util(): Base64Util{
        checkContext()
        return Base64Util.invoke
    }

    /**
     * [Bitmap]工具类
     */
    fun bitmapUtil(): BitmapUtil{
        checkContext()
        return BitmapUtil.invoke
    }

    /**
     * 缓存工具类
     */
    fun cacheUtil(): CacheUtil{
        checkContext()
        return CacheUtil.invoke
    }

    /**
     * 剪切板工具类
     */
    fun clipBoardUtil(): ClipBoardUtil{
        checkContext()
        return ClipBoardUtil.invoke
    }

    /**
     * 异常捕获
     */
    fun crashHandle(message: String = "应用出现异常，即将重启", crashCallBack: (Throwable) ->Unit = {}){
        checkContext()
        CustomCrashHandler.init(message, crashCallBack)
    }

    /**
     * html工具类
     */
    fun fromHtml(html: String, getter: Html.ImageGetter? = null): Spanned {
        checkContext()
        return CustomHtml.invoke().fromHtml(html, getter)
    }

    /**
     * 时间格式化工具类
     */
    fun dateUtil(): DateFormatUtil{
        checkContext()
        return DateFormatUtil.invoke
    }

    /**
     * 文件工具类
     */
    fun fileUtil(): FileUtil{
        checkContext()
        return FileUtil.invoke
    }

    /**
     * gson工具类
     */
    fun gsonUtil(): GsonUtils{
        checkContext()
        return GsonUtils.invoke
    }

    /**
     * 键盘工具类
     */
    fun keyBoardUtil(): KeyBoardUtil{
        checkContext()
        return KeyBoardUtil.invoke
    }

    /**
     * 日志工具类
     */
    fun logUtil(): LogUtil{
        checkContext()
        return LogUtil.invoke
    }

    /**
     * MD5工具类
     */
    fun md5Util(): MD5Util{
        checkContext()
        return MD5Util.invoke
    }

    /**
     * mmkv工具类
     */
    fun mmkvUtil(): MMKVUtil{
        checkContext()
        return MMKVUtil.invoke
    }

    /**
     * 网络工具类
     */
    fun netWorkUtil(): NetUtil {
        checkContext()
        return NetUtil.invoke
    }

    /**
     * 正则工具类
     */
    fun regexUtil(): RegexUtil{
        checkContext()
        return RegexUtil.invoke
    }

    /**
     * 系统工具类
     */
    fun romUtil(): RomUtil{
        checkContext()
        return RomUtil.invoke
    }

    /**
     * 屏幕工具类
     */
    fun screenUtil(): ScreenUtil{
        checkContext()
        return ScreenUtil.invoke
    }

    /**
     * 内存卡工具类
     */
    fun sdCardUtil(): SDCardUtil{
        checkContext()
        return SDCardUtil.invoke
    }

    /**
     * 状态栏工具类
     */
    fun statusBarUtil(): StatusBarUtil{
        checkContext()
        return StatusBarUtil.invoke
    }

    /**
     * 倒计时工具
     */
    fun timer(totalMillis: Long = 60 * 1000L,
              intervalMillis: Long = 60 * 1000L,
              interval: (Long)->Unit,
              finish:()->Unit){
        TimerUtils.start(totalMillis, intervalMillis, interval, finish)
    }

    /**
     * 倒计时工具
     */
    fun timer(): TimerUtils{
        checkContext()
        return TimerUtils.invoke
    }

    /**
     * [Toast]工具类
     */
    fun toast(): ToastUtil{
        checkContext()
        return ToastUtil.invoke
    }

    /**
     * 防止内存溢出的[Handler]
     */
    fun weakHandler(looper: Looper = Looper.getMainLooper()) = WeakHandler(looper)

    /**
     * [WebView]工具类
     */
    fun webViewDelegate() = WebViewDelegate.getInstance()


    /**
     * 获取线程池
     */
    fun threadPool() = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    /**
     * 校验上下文对象
     */
    private fun checkContext(){
        if (appContext == null){
            throw  IllegalArgumentException("context 上下文不能为空 请初始化 Util")
        }
    }

}

