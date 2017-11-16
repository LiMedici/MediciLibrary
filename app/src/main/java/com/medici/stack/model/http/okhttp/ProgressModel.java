package com.medici.stack.model.http.okhttp;

import java.io.Serializable;

/**
 * @desc UI进度回调实体类
 * @author 李宗好
 * @time:2017年1月5日 下午3:08:28
 */
public class ProgressModel implements Serializable {
    //当前读取字节长度
    private long currentBytes;
    //总字节长度
    private long contentLength;
    //是否读取完成
    private boolean done;

    public ProgressModel(long currentBytes, long contentLength, boolean done) {
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "ProgressModel{" +
                "currentBytes=" + currentBytes +
                ", contentLength=" + contentLength +
                ", done=" + done +
                '}';
    }
}
