package com.medici.stack.factory;

import android.support.annotation.NonNull;

import com.medici.stack.Common;
import com.medici.stack.R;
import com.medici.stack.model.data.DataSource;
import com.medici.stack.model.data.HttpTipError;

/**
 * **********************************
 *
 * @desc 数据请求错误统一分发处理
 *
 * **********************************
 */
public class Factory {
    /**
     * 单例模式
     */
    private static final Factory instance;

    static {
        instance = new Factory();
    }

    private Factory() {
    }

    /**
     * 统一处理网络请求错误码
     * @param code 请求错误码
     * @param callback 失败回调对象
     */
    public static void decodeRspCode(int code, @NonNull DataSource.FailedCallback callback) {
        int stringRes = R.string.data_request_failed;
        switch (code) {
            case Common.Constance.ERROR_UNKNOWN:
                stringRes = R.string.data_request_failed;
                break;
        }

        if(null != callback){
            callback.onDataNotAvailable(HttpTipError.newError(stringRes));
        }
    }
}
