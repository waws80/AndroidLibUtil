package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import pw.androidthanatos.lib.utils.LibUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

/**
 * 功能描述: Bitmap工具类
 * @className: BitmapUtil.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午2:17
 */
class BitmapUtil private constructor(private val toastUtil: ToastUtil,
                 private val fileUtil: FileUtil,
                 private val application: Application) {

    companion object {
        val invoke = BitmapUtil(LibUtils.toast(),
            LibUtils.fileUtil(),
            LibUtils.getApplication())
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @param quality 压缩百分比 0 -100
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?, quality: Int = 100): String? {
        var q = quality
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                var value = bitmap.rowBytes.toDouble() * bitmap.height.toDouble() * 1.0
                value = value / 1024.0 / 1024.0
                if (value <= 10.0) {
                    q = 100
                }
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, q, baos)
                baos.flush()
                baos.close()

                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileUtil.close(baos)
        }
        return result
    }

    /**
     * 保存图片
     */
    fun saveBitmap(dir: String, bmSave: Bitmap, fileName: String, isShowToast: Boolean = true, saveCall:(String)->Unit = {}): File? {
        return fileUtil.saveBitmap(dir, bmSave, fileName)?.apply {
            scanPhoto(this)
            saveCall.invoke(dir)
            if (isShowToast) {
                toastUtil.show("图片已保存至$dir")
            }
        }
    }

    /**
     * 通知系统，刷新相册
     */
    private fun scanPhoto(file: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        application.sendBroadcast(mediaScanIntent)
    }

}