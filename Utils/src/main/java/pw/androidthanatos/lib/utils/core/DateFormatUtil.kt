package pw.androidthanatos.lib.utils.core

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 功能描述: 日期工具类
 * @className: DateUtil
 * @author: thanatos
 * @createTime: 2017/11/24
 * @updateTime: 2017/11/24 上午9:02
 */
class DateFormatUtil private constructor(){

    companion object {

        val invoke = DateFormatUtil()
    }

    /**
     * 日期区间获取类
     * @return 0：源日期列表 1：显示日期列表（从开始日期到结束日期，）
     */
    fun getDateRes(yearCount: Int): Array<Any> {
        val start = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val sdfe = SimpleDateFormat("EEEE", Locale.CHINA)
        try {
            val calendar = Calendar.getInstance()
            calendar.time = start
            calendar.add(Calendar.YEAR, yearCount)
            val dEnd = calendar.time
            val listDate = getDatesBetweenTwoDate(start, dEnd)
            val res = ArrayList<String>()
            for (i in listDate.indices) {
                val msg = sdf.format(listDate[i]) + "-" + sdfe.format(listDate[i])
                val arr = msg.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                arr[3] = arr[3].replace("星期", "周")
                res.add(arr[1] + "月" + arr[2] + "日 " + arr[3])
            }
            return arrayOf(listDate, res)
        } catch (e: Exception) {
            e.printStackTrace()
            return arrayOf(2)
        }

    }


    /**
     * 获取格式化后的date
     * @param date 格式化前的date
     * @return 标准的date字符串
     */
    fun getDateFormat(date: String): String {
        val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return try {
            sf.format(sf.parse(date))
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }

    }

    /**
     * 获取格式化后的date
     * @param time 时间戳
     * @return 标准的date字符串
     */
    fun getDateFormat(format: String, time: Long): String {
        val sf = SimpleDateFormat(format, Locale.CHINA)
        return try {
            val date = Date()
            date.time = time
            sf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return List
     */
    private fun getDatesBetweenTwoDate(beginDate: Date, endDate: Date): List<Date> {
        val lDate = ArrayList<Date>()
        lDate.add(beginDate)// 把开始时间加入集合
        val cal = Calendar.getInstance()
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.time = beginDate
        while (true) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1)
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.time)) {
                lDate.add(cal.time)
            } else {
                break
            }
        }
        lDate.add(endDate)// 把结束时间加入集合
        return lDate
    }


    /**
     * 判断前者时间是否在后者时间之前
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 0： 开始时间在结束时间之前 1：开始时间在结束时间之后 2：两个时间相等
     */
    fun isBeginBeforeEnd(beginTime: String, endTime: String): Boolean {
        val begin = DateFormat.getTimeInstance().parse(beginTime)
        val end = DateFormat.getTimeInstance().parse(endTime)
        return begin.before(end)
    }

    /**
     * 传入一个时间戳 返回：格式化的日期
     * @param longTime  时间戳
     * @return 字符串格式化的日期
     */
    fun parseTimeStamp(longTime: Any): String {
        try {
            val time = java.lang.Long.valueOf(longTime.toString())
            val target = Calendar.getInstance()
            target.timeInMillis = time
            val curr = Calendar.getInstance()
            if (curr.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {//今年

                val offset = day(time)
                if (offset == 0) {//今天
                    val d = System.currentTimeMillis() / (1000.0 * 60.0 * 60.0) - time / (1000.0 * 60.0 * 60.0)
                    if (d in 1.0..24.0) {
                        return d.toInt().toString() + "小时前"
                    } else if (d < 1) {
                        val s = (d * 60).toInt()
                        return if (s <= 1) {
                            "刚刚"
                        } else {
                            s.toString() + "分钟前"
                        }
                    }
                } else return when (offset) {
                    1 -> //昨天
                        "昨天" + getDateFormat("HH:mm", time)
                    2 -> //前天
                        "前天" + getDateFormat("HH:mm", time)
                    else -> //更早
                        getDateFormat("MM-dd HH:mm", time)
                }
            } else {//不是今年
                return getDateFormat("yyyy-MM-dd HH:mm", time)
            }
        } catch (e: Exception) {
            return longTime.toString()
        }
        return longTime.toString()
    }

    /**
     * 获取时间戳
     * @param formatData 格式化的时间
     * @return
     */
    fun getTimeStamp(formatData: String): Long {
        val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return try {
            val time = sf.parse(formatData).time
            time
        } catch (e: ParseException) {
            e.printStackTrace()
            -1L
        }
    }


    /**
     * 判断 今天 昨天  前天  更早 0  ， 1  ，2， 3
     * @param timeStamp
     * @return  今天 昨天  前天  更早 0  ， 1  ，2， 3
     */
    private fun day(timeStamp: Long): Int {
        val curTimeMillis = System.currentTimeMillis()
        val curDate = Date(curTimeMillis)
        val todayHoursSeconds = curDate.hours * 60 * 60
        val todayMinutesSeconds = curDate.minutes * 60
        val todaySeconds = curDate.seconds
        val todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000
        val todayStartMillis = curTimeMillis - todayMillis
        if (timeStamp >= todayStartMillis) {
            return 0
        }
        val oneDayMillis = 24 * 60 * 60 * 1000
        val yesterdayStartMillis = todayStartMillis - oneDayMillis
        if (timeStamp >= yesterdayStartMillis) {
            return 1
        }
        val yesterdayBeforeStartMillis = yesterdayStartMillis - oneDayMillis
        return if (timeStamp >= yesterdayBeforeStartMillis) {
            2
        } else 3
    }
}
