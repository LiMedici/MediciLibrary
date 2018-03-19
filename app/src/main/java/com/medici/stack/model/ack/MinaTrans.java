package com.medici.stack.model.ack;

import com.google.common.collect.Maps;

import java.util.Map;


/**
 * @Desc 专门用来处理事物的类
 * @author 李宗好
 * @time:2017年2月20日下午6:10:39
 */
public class MinaTrans {

	private static final String KEY_SEQUENCE = "sequenceId";
	
	private static Map<Long,TransactionInfo>  mTransMap = null;
	private static MinaTrans INSTANCE = null;
	
	static{
		mTransMap = Maps.newConcurrentMap();
	}
	
	private MinaTrans(){
		//开启子线程检查是否超时
		new OutThread(mTransMap).start();
	}
	
	public static MinaTrans newInstance(){
		if(INSTANCE == null){
			synchronized (MinaTrans.class) {
				if(INSTANCE == null){
					INSTANCE = new MinaTrans();
				}
			}
		}
		return INSTANCE;
	}

	/**
	 * 发送请求,执行事物
	 * @param transInfo 事物信息
	 * @return 事物信息
	 */
	public Disposable post(TransactionInfo transInfo){
		long sequenceId = transInfo.getSequenceId();
		mTransMap.put(sequenceId, transInfo);
		transInfo.getCallback().execute(sequenceId);
		return new ObserverDisposable(transInfo);
	}
	
	/**
	 * @return 返回递增的Id
	 */
	public static long generate(){
		return mTransMap.size()+1L;
	}
	
	/**
	 * 处理消息
	 * @param model AckModel数据
	 * @return 返回是否成功响应内容
	 */
	public static <T> boolean handleTrans(AckModel<T> model){

		if(model == null || model.getSequenceId() == null)
			return false;

		long sequenceId = model.getSequenceId();

		if(sequenceId <= 0){
			return false;
		}
		
		TransactionInfo transactionInfo = mTransMap.remove(sequenceId);

		// 回调获取到的数据
		if(null != transactionInfo && null != transactionInfo.getCallback()){
			if(model.getContent() != null){
				transactionInfo.getCallback().onSucceed(model.getContent());
				return true;
			}
		}
		
		return false;
	}
	
}
