package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: MinaCallback的实现类,实现接口方法。不关心超时函数
 *
 * ***************************************
 */
public abstract class AckCallback<T> implements MinaCallback<T>{

    @Override
    public void timeOut() {

    }
}
