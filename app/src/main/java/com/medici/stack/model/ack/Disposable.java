package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: 用来判断Mina事物的当前状态,和取消请求
 * @author：李宗好
 * @time: 2018/1/16 0016 08:43
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
 *
 * ***************************************
 */
public interface Disposable {

    /**
     * Dispose the resource, the operation should be idempotent.
     */
    void dispose();

    /**
     * Returns true if this resource has been disposed.
     * @return true if this resource has been disposed
     */
    boolean isDisposed();
}
