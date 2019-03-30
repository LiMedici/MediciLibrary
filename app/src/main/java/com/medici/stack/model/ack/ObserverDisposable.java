package com.medici.stack.model.ack;

/**
 * ***************************************
 *
 * @desc: 任务的生命周期管理者
 *
 *
 * ***************************************
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
