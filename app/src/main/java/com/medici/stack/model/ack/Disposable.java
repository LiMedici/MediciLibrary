package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: 用来判断Mina事物的当前状态,和取消请求
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
