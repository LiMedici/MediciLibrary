package com.medici.stack.model.rxjava;

import com.orhanobut.logger.Logger;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * desc:Rxjava,错误回调接口
 * author：李宗好
 * time: 2017/6/14 14:46
 * ic_email_icon：lzh@cnbisoft.com
 */
public abstract class RxErrorConsumer implements Consumer<Throwable> {

    @Override
    public void accept(@NonNull Throwable throwable) throws Exception {
        String message = "";
        if(throwable instanceof RxServerException){
            message = throwable.getMessage();
        }else if(throwable instanceof SocketTimeoutException){
            message = "请求超时...";
        }else if(throwable instanceof UnknownHostException){
            message = "网络异常...";
        }else{
            message = "请求失败,请稍候尝试...";
        }

        Logger.e(throwable,this.getClass().getCanonicalName()+" ========== RxError");

        onError(message);
    }

    public abstract void onError(String message);
}
