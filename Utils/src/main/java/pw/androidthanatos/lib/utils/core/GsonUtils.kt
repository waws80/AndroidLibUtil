package pw.androidthanatos.lib.utils.core

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

import java.util.ArrayList

/**
 * @desc: Gson 工具类
 * @className: GsonUtils
 * @author: thanatos
 * @createTime: 2018/10/19
 */
class GsonUtils private constructor(){

    companion object {

        val invoke = GsonUtils()
    }

    /**
     * 返回一个gson
     * @return
     */
    val gson: Gson = GsonBuilder()
            .registerTypeAdapter(String::class.java, StringAdapter())
            .registerTypeAdapter(Int::class.java, IntAdapter())
            .registerTypeAdapter(Long::class.java, LongAdapter())
            .registerTypeAdapter(Double::class.java, DoubleAdapter())
            .registerTypeAdapter(Boolean::class.java, BoolAdapter())
            .registerTypeAdapter(Float::class.java, FloatAdapter())
            .registerTypeAdapterFactory(object : TypeAdapterFactory{
                override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>): TypeAdapter<T>? {
                    val rawType = type.rawType
                    if (rawType != String::class.java){
                        return null
                    }
                    @Suppress("UNCHECKED_CAST")
                    return StringNoNullAdapter() as TypeAdapter<T>
                }

            })
            .create()

    /**
     * 节点得到相应的内容
     * @param jsonString json字符串
     * @param note       节点
     * @return 节点对应的内容
     */
    fun getNoteJsonString(jsonString: String?, note: String): String {
        val element = JsonParser().parse(jsonString)
        if (element.isJsonNull) {
            throw RuntimeException("得到的jsonElement对象为空")
        }
        return element.asJsonObject.get(note).toString()
    }

    /**
     * 节点得到节点内容，转化为一个数组
     * @param jsonString json字符串
     * @return 含有目标对象的集合
     */
    fun <T> convertList(clz: Class<T>, jsonString: String?, note: String = ""): List<T> {
        val json: String = if (note.isEmpty()){
            jsonString?:""
        }else{
            getNoteJsonString(jsonString, note)
        }
        if (json.isEmpty()){
            return emptyList()
        }
        val jsonElement = JsonParser().parse(json)
        if (jsonElement.isJsonNull) {
            return emptyList()
        }
        if (!jsonElement.isJsonArray) {
            throw RuntimeException("json字符不是一个数组对象集合")
        }
        val jsonArray = jsonElement.asJsonArray
        val beans = ArrayList<T>()
        for (jsonElement2 in jsonArray) {
            val bean = gson.fromJson(jsonElement2, clz)
            beans.add(bean)
        }
        return beans
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param note       json标签
     * @return 含有目标对象的集合
     */
    fun <T> convertObject(clz: Class<T>, jsonString: String?, note: String = ""): T {
        val json: String = if (note.isEmpty()){
            jsonString?:""
        }else{
            getNoteJsonString(jsonString, note)
        }
        if (json.isEmpty()){
            return clz.newInstance()
        }
        val jsonElement = JsonParser().parse(json)
        if (jsonElement.isJsonNull) {
            return clz.newInstance()
        }
        if (!jsonElement.isJsonObject) {
            throw RuntimeException("json不是一个对象")
        }
        return gson.fromJson(jsonElement, clz)
    }


    /**
     * 节点得到节点内容，转化为一个数组
     * @param jsonString json字符串
     * @return 含有目标对象的集合
     */
    inline fun <reified T> convertList(jsonString: String, note: String = ""): List<T> {
        return convertList(T::class.java,jsonString, note)
    }

    /**
     * 按照节点得到节点内容，转化为一个数组
     *
     * @param jsonString json字符串
     * @param note       json标签
     * @return 含有目标对象的集合
     */
    inline fun <reified T> convertObject(jsonString: String, note: String = ""): T {
        return convertObject(T::class.java, jsonString, note)
    }


    /**
     * 把bean对象转化为json字符串
     * @param obj bean对象
     * @return 返回的是json字符串
     */
    fun toJson(obj: Any?): String {
        return gson.toJson(obj)
    }

    class IntAdapter : JsonSerializer<Int>, JsonDeserializer<Int>{
        override fun serialize(src: Int, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Int {
            try {
                if (json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return 0
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            try {
                return json.asInt
            }catch (e: Exception){
                if (e is NumberFormatException){
                    return try {
                        json.asFloat.toInt()
                    }catch (e: Exception){
                        if (e is NumberFormatException){
                            try {
                                json.asDouble.toInt()
                            }catch (e: Exception){
                                throw IllegalArgumentException(e)
                            }
                        }else{
                            throw IllegalArgumentException(e)
                        }
                    }
                }else{
                    throw IllegalArgumentException(e)
                }

            }
        }

    }

    class FloatAdapter : JsonSerializer<Float>, JsonDeserializer<Float>{
        override fun serialize(src: Float, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Float {
            try {
                if (json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return 0f
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            try {
                return json.asFloat
            }catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }

    }

    class DoubleAdapter : JsonSerializer<Double>, JsonDeserializer<Double>{
        override fun serialize(src: Double, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Double {
            try {
                if (json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return 0.00
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            try {
                return json.asDouble
            }catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }

    }

    class LongAdapter : JsonSerializer<Long>, JsonDeserializer<Long>{
        override fun serialize(src: Long, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Long {
            try {
                if (json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return 0L
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            try {
                return json.asLong
            }catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }

    }

    class BoolAdapter : JsonSerializer<Boolean>, JsonDeserializer<Boolean>{
        override fun serialize(src: Boolean, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Boolean {
            try {
                if (json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return false
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
            try {
                return json.asBoolean
            }catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }

    }

    class StringAdapter(private val def: String = "") : JsonSerializer<String>, JsonDeserializer<String>{
        override fun serialize(src: String?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src == null){
                return JsonPrimitive(def)
            }
            return JsonPrimitive(src)
        }

        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
            try {
                if (json?.asString == null || json.asString == "" || json.asString.isNullOrEmpty()
                    || json.asString == "null" || json.asString == "NULL"){
                    return def
                }
            }catch (e: Exception){
                e.printStackTrace()
                return def
            }
            try {
                return json.asString!!
            }catch (e: Exception){
                throw IllegalArgumentException(e)
            }
        }

    }

    class StringNoNullAdapter(private val def: String = "") : TypeAdapter<String>(){
        override fun write(out: JsonWriter, value: String?) {
            if (null == value){
                out.nullValue()
                return
            }
            out.value(value)
        }

        override fun read(reader: JsonReader): String {
            if (reader.peek() == JsonToken.NULL){
                reader.nextNull()
                return def
            }
            return  reader.nextString()
        }

    }

}

