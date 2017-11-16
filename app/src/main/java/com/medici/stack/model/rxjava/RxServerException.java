package com.medici.stack.model.rxjava;

/**
 * desc: 自定义的http请求服务器异常
 * author：李宗好
 * time: 2017/06/14 11:15
 * ic_email_icon：lzh@cnbisoft.com
 */
public class RxServerException extends Throwable {

    public RxServerException(String message) {
        super(message);
    }

}
