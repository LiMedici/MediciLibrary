package com.medici.stack.model.rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * desc: Rxjava的转换器Transformer
 * author：李宗好
 * time: 2017/06/03 15:16
 * ic_email_icon：lzh@cnbisoft.com
 */
public class RxSchedulersHelper {

    public static <T> ObservableTransformer<T, T> io2Main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return io2Main();
    }

}
