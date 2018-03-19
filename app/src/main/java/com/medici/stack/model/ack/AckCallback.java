package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: MinaCallback的实现类,实现接口方法。不关心超时函数
 * @author：李宗好
 * @time: 2018/1/16 0016 10:01
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
 *
 * ***************************************
 */
public abstract class AckCallback<T> implements MinaCallback<T>{

    @Override
    public void timeOut() {

    }
}
