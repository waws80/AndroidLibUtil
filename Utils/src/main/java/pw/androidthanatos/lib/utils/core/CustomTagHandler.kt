package pw.androidthanatos.lib.utils.core

import android.graphics.Color
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import org.xml.sax.XMLReader

import java.util.HashMap

/**
 * 功能描述: 自定义的html 标签解析器
 * @className: CustomTagHandler
 * @author: thanatos
 * @createTime: 2018/2/23
 * @updateTime: 2018/2/23 09:27
 */
internal class CustomTagHandler : Html.TagHandler {

    /**
     * 自定义的删除线标签
     *
     *
     * <cs>我是删除线</cs> 最好使用在html最后 或者 一个新的文本框
     *
     * cs节点下标标记
     */
    private var csStartIndex = 0
    private var cFontStartIndex = 0

    /**
     * 自定义的font标签支持 设置字体大小
     *
     *
     * <cfont size='20px' color='#FFFFFF'>我是自定义font</cfont>
     *
     * cfont节点下标标记
     */
    private var csEndIndex = 0
    private var cFontEndIndex = 0

    /**
     * cfont节点属性存储map
     */
    private val attributes = HashMap<String, String>()


    override fun handleTag(opening: Boolean, tag: String, output: Editable, xmlReader: XMLReader) {
        if (tag == "cs") {
            if (opening) {
                startTag(tag, output)
            } else {
                endTad(tag, output)
            }
        } else if (tag == "cfont") {
            parseAttribute(xmlReader)
            if (opening) {
                startTag(tag, output)
            } else {
                endTad(tag, output)
            }
        }

    }

    /**
     * 标记节点开始下标
     * @param tag
     * @param outPut
     */
    private fun startTag(tag: String, outPut: Editable) {
        if (tag == "cs") {
            csStartIndex = outPut.length
        } else if (tag == "cfont") {
            cFontStartIndex = outPut.length
        }

    }

    /**
     * 节点结束下标并进行属性处理
     * @param tag
     * @param outPut
     */
    private fun endTad(tag: String, outPut: Editable) {
        if (tag == "cs") {
            csEndIndex = outPut.length
            outPut.setSpan(
                StrikethroughSpan(), csStartIndex, csEndIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else if (tag == "cfont") {
            cFontEndIndex = outPut.length
            val color = attributes["color"]
            val attr = attributes["size"]
            val size: String
            if (attr != null && attr.contains("px")) {
                size = attr.split("px".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } else {
                size = ""
            }

            setAttribute(color, size, outPut, cFontStartIndex, cFontEndIndex)
        }

    }

    private fun setAttribute(color: String?, size: String, outPut: Editable, cfontStrtIndex: Int, cfontEndIndex: Int) {
        if (!TextUtils.isEmpty(color)) {
            outPut.setSpan(
                ForegroundColorSpan(Color.parseColor(color)), cfontStrtIndex,
                cfontEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        if (!TextUtils.isEmpty(size)) {
            outPut.setSpan(
                AbsoluteSizeSpan(Integer.parseInt(size)), cfontStrtIndex,
                cfontEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    /**
     * 解析节点属性
     * @param xmlReader
     */
    private fun parseAttribute(xmlReader: XMLReader) {
        try {
            val elementField = xmlReader.javaClass.getDeclaredField("theNewElement")
            elementField.isAccessible = true
            val element = elementField.get(xmlReader)
            val attsField = element.javaClass.getDeclaredField("theAtts")
            attsField.isAccessible = true
            val atts = attsField.get(element)
            val dataField = atts.javaClass.getDeclaredField("data")
            dataField.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val data = dataField.get(atts) as Array<String>
            val lengthField = atts.javaClass.getDeclaredField("length")
            lengthField.isAccessible = true
            val len = lengthField.get(atts) as Int

            for (i in 0 until len) {
                attributes[data[i * 5 + 1]] = data[i * 5 + 4]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
