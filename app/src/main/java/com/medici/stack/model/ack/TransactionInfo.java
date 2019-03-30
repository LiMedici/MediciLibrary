package com.medici.stack.model.ack;


/**
 * @desc 要处理的请求的事物信息
 */
public class TransactionInfo {
	
	private long sequenceId;
	private long createTime;
	private long timeOut;

	/**
	 * 该事物是否已经取消释放?
	 */
	private boolean dispose;

	private MinaCallback callback;
	
	public TransactionInfo(long sequenceId,long timeOut,MinaCallback callback){
		this.sequenceId = sequenceId;
		this.timeOut = timeOut;
		this.createTime = System.currentTimeMillis();
		this.callback = callback;
	}
	
	public long getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(long sequenceId) {
		this.sequenceId = sequenceId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	public MinaCallback getCallback() {
		return callback;
	}
	public void setCallback(MinaCallback callback) {
		this.callback = callback;
	}
	public boolean isDispose() {
		return dispose;
	}
	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}
}
