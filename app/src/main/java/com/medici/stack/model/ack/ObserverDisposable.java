package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc:
 * @author：李宗好
 * @time: 2018/1/16 0016 09:29
 * @email：lzh@cnbisoft.com
 * @version：
 * @history: ***************************************
 */
public class ObserverDisposable implements Disposable{

    private TransactionInfo info;

    public ObserverDisposable(TransactionInfo info){
        this.info = info;
    }

    @Override
    public void dispose() {
        if(null != info){
            info.setDispose(true);
        }
    }

    @Override
    public boolean isDisposed() {
        return info.isDispose();
    }
}
