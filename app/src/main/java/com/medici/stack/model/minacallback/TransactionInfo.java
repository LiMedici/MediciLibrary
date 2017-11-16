package com.medici.stack.model.minacallback;


import java.util.Date;

/**
 * 要处理的请求的事物信息
 * @Desc TODO
 * @author 李宗好
 * @time:2017年2月20日下午4:48:39
 */
public class TransactionInfo {
	
	private long sequenceId;
	private long createTime = new Date().getTime();
	private long timeOut;
	private CmdCallback callback;
	
	public TransactionInfo(long sequenceId,long timeOut,CmdCallback callback){
		this.sequenceId = sequenceId;
		this.timeOut = timeOut;
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
	public CmdCallback getCallback() {
		return callback;
	}
	public void setCallback(CmdCallback callback) {
		this.callback = callback;
	}
}
