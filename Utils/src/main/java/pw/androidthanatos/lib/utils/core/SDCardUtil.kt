package pw.androidthanatos.lib.utils.core

import android.os.Build
import android.os.Environment
import android.os.StatFs

/**
 * 功能描述: SDCard工具类
 * @className: SDCardUtil.kt
 * @author: thanatos
 * @createTime: 19-4-3
 * @updateTime: 19-4-3 下午2:19
 */
class SDCardUtil {

    companion object {

        val invoke = SDCardUtil()

    }

    /**
     * 获取sdcard卡剩余空间
     * 取得SD卡文件路径
     * 获取单个数据块的大小(Byte)
     * 空闲的数据块的数量
     * 返回SD卡空闲大小
     * return freeBlocks * blockSize; //单位Byte
     * return (freeBlocks * blockSize)/1024; //单位KB
     * 单位MB
     * @return
     */
    fun sdAvaliableSize(): Long{
        val path = Environment.getExternalStorageDirectory()
        val sf = StatFs(path.path)
        val blockSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sf.blockSizeLong
        } else {
            sf.blockSize.toLong()
        }
        val availableBlocks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sf.availableBlocksLong
        } else {
            sf.availableBlocks.toLong()
        }
        return availableBlocks * blockSize / 1024 / 1024
    }

    /**
     * 获取sdcard卡总容量
     *
     * @return
     */
    // 取得SD卡文件路径
    // 获取单个数据块的大小(Byte)
    // 获取所有数据块数
    // 返回SD卡大小
    // return allBlocks * blockSize; //单位Byte
    // return (allBlocks * blockSize)/1024; //单位KB
    // 单位MB
    fun sdTotalSize(): Long{
        val path = Environment.getExternalStorageDirectory()
        val sf = StatFs(path.path)
        val blockSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sf.blockSizeLong
        } else {
            sf.blockSize.toLong()
        }
        val totalBlocks = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            sf.blockCountLong
        } else {
            sf.blockCount.toLong()
        }
        return totalBlocks * blockSize / 1024 / 1024
    }

    /**
     * 检测sdcard是否存在
     *
     * @return
     */
    fun sdcardMounted() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}