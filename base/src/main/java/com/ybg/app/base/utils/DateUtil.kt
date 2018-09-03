package com.ybg.app.base.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     *
     * @return Date 日期
     */
    @JvmOverloads fun parseDate(dateStr: String, format: String = "yyyy-MM-dd"): Date? {
        try {
            val dateFormat = SimpleDateFormat(format, Locale.CHINA)
            return dateFormat.parse(dateStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 功能描述：格式化输出日期
     *
     * @param date   Date 日期
     * @param format String 格式
     *
     * @return 返回字符型日期
     */
    @JvmOverloads fun format(date: Date?, format: String = "yyyy-MM-dd"): String {
        try {
            if (date != null) {
                val dateFormat = SimpleDateFormat(format, Locale.CHINA)
                return dateFormat.format(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 功能描述：返回年份
     *
     * @param date Date 日期
     *
     * @return 返回年份
     */
    fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.YEAR)
    }

    /**
     * 功能描述：返回月份
     *
     * @param date Date 日期
     *
     * @return 返回月份
     */
    fun getMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.MONTH) + 1
    }

    /**
     * 功能描述：返回日份
     *
     * @param date Date 日期
     *
     * @return 返回日份
     */
    fun getDay(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 功能描述：返回小时
     *
     * @param date 日期
     *
     * @return 返回小时
     */
    fun getHour(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date 日期
     *
     * @return 返回分钟
     */
    fun getMinute(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.MINUTE)
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     *
     * @return 返回秒钟
     */
    fun getSecond(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.get(Calendar.SECOND)
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date 日期
     *
     * @return 返回毫秒
     */
    fun getMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar!!.time = date
        return calendar.timeInMillis
    }

    /**
     * 功能描述：返回字符型日期
     *
     * @param date 日期
     *
     * @return 返回字符型日期 yyyy-MM-dd 格式
     */
    fun getDate(date: Date): String {
        return format(date, "yyyy-MM-dd")
    }

    /**
     * 功能描述：返回字符型时间
     *
     * @param date Date 日期
     *
     * @return 返回字符型时间 HH:mm:ss 格式
     */
    fun getTime(date: Date): String {
        return format(date, "HH:mm:ss")
    }

    /**
     * 功能描述：返回字符型日期时间
     *
     * @param date Date 日期
     *
     * @return 返回字符型日期时间 yyyy-MM-dd HH:mm:ss 格式
     */
    fun getDateTime(date: Date): String {
        return format(date, "yyyy-MM-dd HH:mm:ss")
    }

    /**
     * 功能描述：日期相加
     *
     * @param date Date 日期
     * @param day  int 天数
     *
     * @return 返回相加后的日期
     */
    fun addDate(date: Date, day: Int): Date {
        val calendar = Calendar.getInstance()
        val millis = getMillis(date) + day.toLong() * 24 * 3600 * 1000
        calendar!!.timeInMillis = millis
        return calendar.time
    }

    /**
     * 功能描述：日期相减
     *
     * @param date  Date 日期
     * @param date1 Date 日期
     *
     * @return 返回相减后的日期
     */
    fun diffDate(date: Date, date1: Date): Int {
        return ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000)).toInt()
    }

    /**
     * 功能描述：取得指定月份的第一天
     *
     * @param strdate String 字符型日期
     *
     * @return String yyyy-MM-dd 格式
     */
    fun getMonthBegin(strdate: String): String {
        val date = parseDate(strdate)
        return format(date, "yyyy-MM") + "-01"
    }

    /**
     * 功能描述：取得指定月份的最后一天
     *
     * @param strdate String 字符型日期
     *
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    fun getMonthEnd(strdate: String): String {
        val date = parseDate(getMonthBegin(strdate))
        val calendar = Calendar.getInstance()
        if (calendar == null) {
            println("calendar is null...")
        }
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return formatDate(calendar.time)
    }

    /**
     * 功能描述：常用的格式化日期
     *
     * @param date Date 日期
     *
     * @return String 日期字符串 yyyy-MM-dd格式
     */
    fun formatDate(date: Date): String {
        return formatDateByFormat(date, "yyyy-MM-dd")
    }

    /**
     * 功能描述：以指定的格式来格式化日期
     *
     * @param date   Date 日期
     * @param format String 格式
     *
     * @return String 日期字符串
     */
    fun formatDateByFormat(date: Date?, format: String): String {
        var result = ""
        if (date != null) {
            try {
                val sdf = SimpleDateFormat(format, Locale.CHINA)
                result = sdf.format(date)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
        return result
    }

    /**
     * 获取某天是星期几
     *
     * @param date
     *
     * @return
     */
    fun getMonthDayWeek(date: Date): String {
        val c = Calendar.getInstance()
        c.time = date
        val year = c.get(Calendar.YEAR)    //获取年
        val month = c.get(Calendar.MONTH) + 1   //获取月份，0表示1月份
        val day = c.get(Calendar.DAY_OF_MONTH)    //获取当前天数
        val week = c.get(Calendar.DAY_OF_WEEK)

        var weekStr: String? = null
        when (week) {

            Calendar.SUNDAY -> weekStr = "周日"

            Calendar.MONDAY -> weekStr = "周一"

            Calendar.TUESDAY -> weekStr = "周二"

            Calendar.WEDNESDAY -> weekStr = "周三"

            Calendar.THURSDAY -> weekStr = "周四"

            Calendar.FRIDAY -> weekStr = "周五"

            Calendar.SATURDAY -> weekStr = "周六"
        }

        return "$month 月$day 日($weekStr)"
    }

    /**
     * 日期字符串转换为日期
     *
     * @param date    日期字符串
     * @param pattern 格式
     *
     * @return 日期
     */
    fun formatStringByFormat(date: String, pattern: String): Date? {
        val sdf = SimpleDateFormat(pattern, Locale.CHINA)
        try {
            return sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获得口头时间字符串，如今天，昨天等
     *
     * @param d 时间格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 口头时间字符串
     */
    fun getTimeInterval(d: String): String {
        val date = formatStringByFormat(d, "yyyy-MM-dd HH:mm:ss")
        val now = Calendar.getInstance()
        now.time = Date()
        val nowYear = now.get(Calendar.YEAR)
        val nowMonth = now.get(Calendar.MONTH)
        val nowWeek = now.get(Calendar.WEEK_OF_MONTH)
        val nowDay = now.get(Calendar.DAY_OF_WEEK)
        val nowHour = now.get(Calendar.HOUR_OF_DAY)
        val nowMinute = now.get(Calendar.MINUTE)

        val ca = Calendar.getInstance()
        if (date != null)
            ca.time = date
        else
            ca.time = Date()
        val year = ca.get(Calendar.YEAR)
        val month = ca.get(Calendar.MONTH)
        val week = ca.get(Calendar.WEEK_OF_MONTH)
        val day = ca.get(Calendar.DAY_OF_WEEK)
        val hour = ca.get(Calendar.HOUR_OF_DAY)
        val minute = ca.get(Calendar.MINUTE)
        if (year != nowYear) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            //不同年份
            return sdf.format(date)
        } else {
            if (month != nowMonth) {
                //不同月份
                val sdf = SimpleDateFormat("M月dd日", Locale.CHINA)
                return sdf.format(date)
            } else {
                if (week != nowWeek) {
                    //不同周
                    val sdf = SimpleDateFormat("M月dd日", Locale.CHINA)
                    return sdf.format(date)
                } else if (day != nowDay) {
                    if (day + 1 == nowDay) {
                        return "昨天" + formatDateByFormat(date, "HH:mm")
                    }
                    if (day + 2 == nowDay) {
                        return "前天" + formatDateByFormat(date, "HH:mm")
                    }
                    //不同天
                    val sdf = SimpleDateFormat("M月dd日", Locale.CHINA)
                    return sdf.format(date)
                } else {
                    //同一天
                    val hourGap = nowHour - hour
                    if (hourGap == 0)
                    //1小时内
                    {
                        if (nowMinute - minute < 1) {
                            return "刚刚"
                        } else {
                            return "${nowMinute - minute}分钟前"
                        }
                    } else if (hourGap in 1..12) {
                        return "$hourGap 小时前"
                    } else {
                        val sdf = SimpleDateFormat("今天 HH:mm", Locale.CHINA)
                        return sdf.format(date)
                    }
                }
            }
        }
    }

    /**
     * 日期字符串转换为日期
     *
     * @param date    日期字符串
     * @param pattern 格式
     *
     * @return 日期
     */
    fun reformatTime(date: String, pattern: String): String {
        val fmt = "yyyy-MM-dd HH:mm:ss"
        val simple = SimpleDateFormat(pattern, Locale.CHINA)
        val old = parseToDate(date, fmt)
        return simple.format(old)
    }

    /**
     * 字符串转换成日期.
     *
     * @param dateString 日期字符
     * @param pattern    格式化.
     *
     * @return
     */
    fun parseToDate(dateString: String, pattern: String?): Date {
        var p: String? = pattern

        if (p == null || "" == p) {
            p = "yyyy-MM-dd HH:mm:ss"
        }

        val formatter = SimpleDateFormat(p, Locale.getDefault())
        try {
            return formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return Date()
    }

    fun getTimeInterval(d: Date): String {
        val date = format(d, "yyyy-MM-dd HH:mm:ss")
        return getTimeInterval(date)
    }

    fun dateDiff(startTime: String, endTime: String, format: String): Array<Long> {
        // 按照传入的格式生成一个simpledateformate对象
        val sd = SimpleDateFormat(format, Locale.CHINA)
        val nd = (1000 * 24 * 60 * 60).toLong()// 一天的毫秒数
        val nh = (1000 * 60 * 60).toLong()// 一小时的毫秒数
        val nm = (1000 * 60).toLong()// 一分钟的毫秒数
        val ns: Long = 1000// 一秒钟的毫秒数
        val diff: Long
        var day: Long = 0
        var hour: Long = 0
        var min: Long = 0
        var sec: Long = 0
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).time - sd.parse(startTime).time
            day = diff / nd// 计算差多少天
            hour = diff % nd / nh + day * 24// 计算差多少小时
            min = diff % nd % nh / nm + day * 24 * 60// 计算差多少分钟
            sec = diff % nd % nh % nm / ns// 计算差多少秒
            // 输出结果
            println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
                    + (min - day * 24 * 60) + "分钟" + sec + "秒。")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val result = arrayOf(day, hour, min, sec)
        return result
    }

}
