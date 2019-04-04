package pw.androidthanatos.lib.utils.core

import android.app.Application
import android.content.Intent
import android.content.res.AssetManager
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import pw.androidthanatos.lib.utils.LibUtils

import java.io.*
import java.math.BigDecimal


/**
 * @desc: 文件工具类
 * @className: FileUtil
 * @author: thanatos
 * @createTime: 2018/10/19
 * @updateTime: 2018/10/19 下午4:09
 */
class FileUtil private constructor(private val application: Application) {

    companion object {

        val invoke = FileUtil(LibUtils.getApplication())
    }

    /**
     * 读取Assets文件内容
     */
    fun getFromAssets(fileName: String): String? {
        return try {
            val inputStream = application.resources.assets.open(fileName)
            String(inputStream.readBytes())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    }

    /**
     * 把Assets里的文件拷贝到sd卡上
     */
    fun copyAssetToSDCard(assetManager: AssetManager = application.assets, fileName: String, outPutFilePath: String): Boolean {
        return try {
            val inputStream: InputStream? = assetManager.open(fileName)
            val os: FileOutputStream? = FileOutputStream(outPutFilePath)
            if (inputStream != null && os != null) {
                os.write(inputStream.readBytes())
                close(os)
                close(inputStream)
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    /**
     * 相对路径转绝对路径
     */
    fun uriToPath(uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            if (uri.scheme!!.equals("file", ignoreCase = true)) {
                return uri.path
            }
            cursor = application.contentResolver
                .query(uri, null, null, null, null)
            return if (cursor!!.moveToFirst()) {
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)) //图片文件路径
            }else{
                null
            }
        } catch (e: Exception) {
            if (null != cursor) {
                cursor.close()
                cursor = null
            }
            return null
        }
    }

    /**
     * 删除文件夹
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir!!.delete()
    }

    /**
     * 获取文件大小
     */
    fun getSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size = if (fileList[i].isDirectory) {
                    size + getSize(fileList[i])
                } else {
                    size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 获取目录名
     */
    fun getFolderName(filePath: String): String {
        if (TextUtils.isEmpty(filePath)) {
            return filePath
        }
        val filePosition = filePath.lastIndexOf(File.separator)
        return if (filePosition == -1) "" else filePath.substring(0, filePosition)
    }

    /**
     * 检查文件是否不大于指定大小
     */
    fun checkFileSize(filepath: String, maxSize: Int): Boolean {
        val file = File(filepath)
        if (!file.exists() || file.isDirectory) {
            return false
        }
        return file.length() <= maxSize * 1024
    }

    /**
     * 格式化文件大小的显示
     */
    fun formatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0K"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }
        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 读取文件
     */
    fun readFile(file: File?): StringBuilder? {
        if (file == null || !file.isFile) {
            return null
        }
        return try {
            val inputStream = InputStreamReader(FileInputStream(file), "UTF-8")
            java.lang.StringBuilder(inputStream.readText())
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 写入文件
     */
    fun writeFile(file: File?, content: String, append: Boolean): Boolean {
        if (file == null || !file.isFile) {
            return false
        }
        if (TextUtils.isEmpty(content)) {
            return false
        }

        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter(file, append)
            fileWriter.write(content)
            return true
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(fileWriter)
        }
    }

    /**
     * 写入文件
     */
    fun writeFile(file: File, stream: InputStream, append: Boolean): Boolean {
        try {
            makeDir(file.absolutePath)
            val o = FileOutputStream(file, append)
            o.write(stream.readBytes())
            o.flush()
            return true
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(stream)
        }
    }

    /**
     * 移动文件
     */
    fun moveFile(srcFile: File, destFile: File) {
        val rename = srcFile.renameTo(destFile)
        if (!rename) {
            copyFile(srcFile.absolutePath, destFile.absolutePath)
            deleteFile(srcFile)
        }
    }

    /**
     * 复制文件
     */
    fun copyFile(sourceFilePath: String, destFilePath: String): Boolean {
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(sourceFilePath)
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        }

        return writeFile(File(destFilePath), inputStream, false)
    }


    /**
     * 删除文件
     */
    fun deleteFile(file: File?): Boolean {
        if (file == null || !file.exists()) {
            return true
        }
        return if (!file.isDirectory) {
            file.delete()
        } else {
            for (f in file.listFiles()) {
                deleteFile(f)
            }
            file.delete()
        }
    }

    /**
     * 创建目录
     */
    fun makeDir(filePath: String): Boolean {
        val folderName = getFolderName(filePath)
        if (TextUtils.isEmpty(folderName)) {
            return false
        }
        val folder = File(folderName)
        return folder.exists() && folder.isDirectory || folder.mkdirs()
    }

    /**
     * 创建文件
     */
    fun makeFile(filePath: String): Boolean {
        val file = File(filePath)
        if (!file.exists()) {
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return false
    }


    /**
     * 创建并返回文件
     * @param fileName 文件名字   eg: /test/a.png
     * @return
     */
    fun createFile(fileName: String): File {
        val file = File(Environment.getExternalStorageDirectory(), fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return file
    }

    /**
     * 关闭流
     */
    fun close(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            }

        }
    }

    /**
     * 保存bitmap
     */
    fun saveBitmap(dir: String, bitmap: Bitmap?, fileName: String): File? {
        if (bitmap == null || TextUtils.isEmpty(fileName)) {
            return null
        }
        val file = File(Environment.getExternalStorageDirectory(), "$dir$fileName.png")
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file
        }

    }

    /**
     * 保存bitmap
     */
    fun saveBitmap(bitmap: Bitmap?, path: String, fileName: String): File? {

        if (bitmap == null || TextUtils.isEmpty(fileName)) {
            return null
        }
        //        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"DevoteQr";
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath, path)
        //        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fName = "$fileName.png"
        val file = File(appDir, fName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            val uri = Uri.fromFile(file)
            application.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return file
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return file
        } finally {
            if (!bitmap.isRecycled) {
                System.gc()
            }
        }
    }

    /**
     * 扫描方式
     */
    enum class ScannerType {
        RECEIVER, MEDIA
    }

    /**
     * Receiver 扫描更新图库
     * @param path
     */
    private fun scannerByReceiver(path: File, fName: String) {
        try {
            MediaStore.Images.Media.insertImage(
                application.contentResolver,
                    path.absolutePath, fName, null)
        } catch (e: FileNotFoundException ) {
            e.printStackTrace()
        }
        // 最后通知图库更新
        application.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(path)))
        //context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$path")))
    }

    /**
     * Media 扫描更新图库
     * @param path
     */
    private fun scannerByMedia(path: String) {
        MediaScannerConnection.scanFile(
            application,
            arrayOf(path), null, null
        )
    }

    /**
     * 保存bitmap
     */
    fun saveBitmapType(bitmap: Bitmap?, path: String, fileName: String, type: ScannerType) {
        if (bitmap == null || TextUtils.isEmpty(fileName)) {
            return
        }
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath, path)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fName = "$fileName.jpg"
        val file = File(appDir, fName)

        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (type == FileUtil.ScannerType.RECEIVER) {
                scannerByReceiver(file, fName)
            } else if (type == FileUtil.ScannerType.MEDIA) {
                scannerByMedia(file.absolutePath)
            }
            if (!bitmap.isRecycled) {
                System.gc()
            }
        }
    }
}
