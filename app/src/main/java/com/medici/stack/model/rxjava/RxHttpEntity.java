package com.medici.stack.model.rxjava;

/**
 * desc:HTTP统一格式返回的包装
 * author：李宗好
 * time: 2017/06/09 16:07
 * ic_email_icon：lzh@cnbisoft.com
 */
public class RxHttpEntity<T>{
    //请求结果成功
    public static final int SUCCESS = 200;

    //请求结果失败
    public static final int ERROR = 0;

    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
