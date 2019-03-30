package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: 自定义回调函数,主要用作Socket的通信确认回调
 *
 * ***************************************
 */
public class AckUtil {

    /**
     * 默认超时时间
     */
    private static final long DEFAULT_TIMEOUT = 3000;

    /**
     * 设置超时时间
     */
    private long timeOut = DEFAULT_TIMEOUT;

    private AckUtil(){}


    /**
     * 执行事物信息
     * @param callback 回调接口
     * @param <T> 泛型参数 返回数据类型的参数
     * @return 该事物的操作行为接口
     */
    public <T> Disposable execute(MinaCallback<T> callback){
        long sequenceId = MinaTrans.generate();
        TransactionInfo info = new TransactionInfo(sequenceId,timeOut,callback);
        return MinaTrans.newInstance().post(info);
    }

    public static class Builder{

        private AckUtil util;

        public Builder(){
            util = new AckUtil();
        }

        public Builder timeOut(long milliseconds) {
            util.timeOut = milliseconds;
            return this;
        }

        public AckUtil build(){
            return util;
        }
    }



}
