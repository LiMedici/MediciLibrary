package com.medici.stack.model.rxjava;

import io.reactivex.functions.Predicate;
import retrofit2.adapter.rxjava2.Result;

/**
 * desc:对返回结果数据判断的PREDICATE
 * author：李宗好
 * time: 2017/06/09 16:01
 * email：lzh@cnbisoft.com
 */
public class RxResults {

    private static Predicate<Result<?>> DATA_PREDICATE = result ->
             !result.isError() && result.response().isSuccessful();

    public static Predicate<Result<?>> isSuccess() {
        return DATA_PREDICATE;
    }
}
