package com.medici.stack.model.http.okhttp;

import com.squareup.okhttp.Callback;

/**
 * @desc 请求体进度回调接口，比如用于文件上传中
 * @author 李宗好
 * @time:2017年1月5日 下午3:02:28
 */
public interface ProgressRequestListener extends Callback{
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
