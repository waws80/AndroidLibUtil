package pw.androidthanatos.lib.utils.core

import android.app.Application
import com.tencent.mmkv.MMKV
import pw.androidthanatos.lib.utils.LibUtils

/**
 * @desc: 腾讯开源的跨进程数据存储 工具类
 * @className: MMKVUtil
 * @author: thanatos
 * @createTime: 2018/10/18
 * @updateTime: 2018/10/18 下午3:14
 */
class MMKVUtil private constructor(private val application: Application){

    companion object {

        private var gsonUtils: GsonUtils? = null

        val invoke = MMKVUtil(LibUtils.getApplication())
    }

    init {
        //初始化mmkv
        MMKV.initialize(application)
    }

    fun mmkv(module: String? = "") = (if (module.isNullOrEmpty())MMKV.defaultMMKV() else MMKV.mmkvWithID(module))!!


    /**
     * 添加数据
     */
    inline fun <reified T: Any> putToMMKV(module: String? = "", value: T, key: String){
        val mv = mmkv(module)
        when(value){
            is Int -> mv.encode(key, value)
            is Double -> mv.encode(key, value)
            is Float -> mv.encode(key, value)
            is Long -> mv.encode(key, value)
            is Boolean -> mv.encode(key, value)
            is String -> mv.encode(key, value)
            is ByteArray -> mv.encode(key, value)
            else -> mv.encode(key, LibUtils.gsonUtil().toJson(value))
        }
    }

    /**
     * 获取数据
     */
    inline fun <reified T> getToMMKV(module: String? = "", key: String, default: T): T{
        val mv = mmkv(module)
        return when(default){
            is Int -> mv.decodeInt(key, default) as T
            is Double -> mv.decodeDouble(key, default) as T
            is Float -> mv.decodeFloat(key, default) as T
            is Long -> mv.decodeLong(key, default) as T
            is Boolean -> mv.decodeBool(key, default) as T
            is String -> mv.decodeString(key, default) as T
            is ByteArray -> mv.decodeBytes(key) as T
            else -> {
                val value =mv.decodeString(key)
                if (value.isNullOrEmpty()){
                    default
                }else{
                    LibUtils.gsonUtil().convertObject(mv.decodeString(key))
                }

            }
        }
    }

    /**
     * 获取集合
     */
    inline fun <reified T> getListToMMKV(module: String? = "", key: String): List<T>{
        val mv = mmkv(module)
        val value = mv.decodeString(key)
        return if (value.isNullOrEmpty()){
            emptyList()
        }else{
            LibUtils.gsonUtil().convertList(value)
        }
    }


    /**
     * 移除数据
     * @param module 模块名字
     * @param key
     */
    fun remove(module: String, key: String) {
        remove(MMKV.mmkvWithID(module, MMKV.MULTI_PROCESS_MODE), key)
    }

    /**
     * 移除数据
     * @param module 模块名字
     * @param keys
     */
    fun removeKeys(module: String, vararg keys: String) {
        removeKeys(MMKV.mmkvWithID(module, MMKV.MULTI_PROCESS_MODE), *keys)
    }

    /**
     * 移除数据
     * @param key
     */
    fun remove(key: String) {
        remove(MMKV.defaultMMKV(), key)
    }

    /**
     * 移除数据
     * @param keys
     */
    fun remove(vararg keys: String) {
        removeKeys(MMKV.defaultMMKV(), *keys)
    }

    /**
     * 清楚数据
     * @param module 模块名字
     */
    fun clear(module: String) {
        clear(MMKV.mmkvWithID(module, MMKV.MULTI_PROCESS_MODE))
    }

    /**
     * 清除数据
     */
    fun clear() {
        clear(MMKV.defaultMMKV())
    }

    private fun remove(mmkv: MMKV, key: String) {
        mmkv.removeValueForKey(key)
    }

    private fun removeKeys(mmkv: MMKV, vararg keys: String) {
        mmkv.removeValuesForKeys(keys)
    }

    private fun clear(mmkv: MMKV) {
        mmkv.clearAll()
    }
}






