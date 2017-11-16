package com.medici.stack.model.http.okhttp;

import com.squareup.okhttp.Callback;

/**
 * @desc 响应体进度回调接口 例如文件下载
 * @author 李宗好
 * @time:2017年1月5日 下午2:56:32
 *
 */
public interface ProgressResponseListener extends Callback{
	void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
