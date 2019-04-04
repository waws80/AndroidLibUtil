package pw.androidthanatos.android.libutil

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import pw.androidthanatos.lib.utils.LibUtils

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LibUtils.appInfo()
        LibUtils.base64Util()
        LibUtils.bitmapUtil()
        LibUtils.cacheUtil()
        LibUtils.clipBoardUtil()
        LibUtils.fromHtml("<span>a<span>")
        LibUtils.crashHandle()
        LibUtils.dateUtil()
        LibUtils.fileUtil()
        LibUtils.gsonUtil()
        LibUtils.keyBoardUtil()
        LibUtils.logUtil()
        LibUtils.md5Util()
        LibUtils.mmkvUtil()
        LibUtils.regexUtil()
        LibUtils.romUtil()
        LibUtils.screenUtil()
        LibUtils.sdCardUtil()
        LibUtils.statusBarUtil()
        LibUtils.timer()
        LibUtils.toast()
        LibUtils.weakHandler()
        LibUtils.webViewDelegate()

    }
}
