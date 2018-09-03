package com.ybg.app.base.constants


class MessageEvent {
    var what: Int = 0
    var obj: Any? = null

    //需要接收状态并传值的时候使用
    constructor(what: Int, obj: Any) {
        this.what = what
        this.obj = obj
    }

    //只需要接收状态不需要接收值情况下使用
    constructor(what: Int) {
        this.what = what
    }

    companion object {
        ///////////////////////////////////////////////////////////////////////////
        // 用一些常量来标识MessageEvent的类型,通过类型来进行相应处理
        ///////////////////////////////////////////////////////////////////////////
        val MESSAGE_USER_LOGIN = 0
        val MESSAGE_USER_LOGOUT = 1
        val MESSAGE_USER_INFO_CHANGE = 2
        val MESSAGE_USER_NEED_LOGIN = 3
        val MESSAGE_SEND_GIFT = 10
        val MESSAGE_SHOW_POST = 20
    }
}
