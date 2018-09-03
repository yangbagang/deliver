package com.ybg.app.base.utils

import java.util.regex.Pattern

/**
 * 一些正则判断
 */
object RegularUtil {
    /**
     * 正则表达式：验证用户名 1-10位
     */
    private val REGEX_USERNAME = "^[a-zA-Z]\\w{1,10}$"

    /**
     * 正则表达式：验证密码 6-20位
     */
    private val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$"

    /**
     * 正则表达式：验证手机号 13,14,15,17,18这些开头的
     */
    private val REGEX_MOBILE = "^1[3|4|5|7|8][0-9]{9}$"

    /**
     * 正则表达式：验证邮箱
     */
    private val REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"

    /**
     * 正则表达式：验证汉字
     */
    private val REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$"

    /**
     * 正则表达式：验证身份证
     */
    private val REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)"

    /**
     * 正则表达式：验证URL
     */
    private val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"

    /**
     * 正则表达式：验证IP地址
     */
    private val REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"

    /**
     * 校验用户名

     * @param username
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isUsername(username: String): Boolean {
        return Pattern.matches(REGEX_USERNAME, username)
    }

    /**
     * 校验密码

     * @param password
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isPassword(password: String): Boolean {
        return Pattern.matches(REGEX_PASSWORD, password)
    }

    /**
     * 校验手机号

     * @param mobile
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isMobile(mobile: String): Boolean {
        return Pattern.matches(REGEX_MOBILE, mobile)
    }

    /**
     * 校验邮箱

     * @param email
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isEmail(email: String): Boolean {
        return Pattern.matches(REGEX_EMAIL, email)
    }

    /**
     * 校验汉字

     * @param chinese
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isChinese(chinese: String): Boolean {
        return Pattern.matches(REGEX_CHINESE, chinese)
    }

    /**
     * 校验身份证

     * @param idCard
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches(REGEX_ID_CARD, idCard)
    }

    /**
     * 校验URL

     * @param url
     * *
     * @return 校验通过返回true，否则返回false
     */
    fun isUrl(url: String): Boolean {
        return Pattern.matches(REGEX_URL, url)
    }

    /**
     * 校验IP地址

     * @param ipAddr
     * *
     * @return
     */
    fun isIPAddr(ipAddr: String): Boolean {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr)
    }

}
