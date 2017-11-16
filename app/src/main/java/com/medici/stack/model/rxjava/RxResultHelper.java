package com.medici.stack.model.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * desc:对使用RxJava http请求回调
 * author：李宗好
 * time: 2017/06/14 11:05
 * ic_email_icon：lzh@cnbisoft.com
 */
public class RxResultHelper<T> {

    public static <T> ObservableTransformer<RxHttpEntity, T> handleResult(Class<T> tClass) {
        return new ObservableTransformer<RxHttpEntity, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<RxHttpEntity> upstream) {
//                return upstream.flatMap(handleResultFunction(tClass));
                return null;
            }
        };
    }

    /**
     * 返回将HttpEntity转换成内部Data数据的Observable
     * @param <T>
     * @return
     */
    public static <T> Function<RxHttpEntity<T>, ObservableSource<T>> handleResultFunction() {
        return httpEntity -> {
            if (httpEntity.getCode() == RxHttpEntity.SUCCESS) {
                if(null == httpEntity.getData()){
                    return Observable.empty();
                }
                return Observable.just(httpEntity.getData());
            } else if(httpEntity.getCode() == RxHttpEntity.ERROR){
                return Observable.error(new RxServerException(httpEntity.getMsg()));
            }
            return Observable.empty();
        };
    }

}
