package com.medici.stack.model.minacallback;

import com.google.common.collect.Maps;

import java.util.Map;


/**
 * 专门用来处理事物的类
 * @Desc TODO
 * @author 李宗好
 * @time:2017年2月20日下午6:10:39
 */
public class MinaTrans {
	
	private static Map<Integer,TransactionInfo> mTransMap = null;
	private static MinaTrans minaTrans = null;
	
	static{
		mTransMap = Maps.newConcurrentMap();
	}
	
	private MinaTrans(){
		//开启子线程检查是否超时
		new OutThread(mTransMap).start();
	}
	
	public static MinaTrans newInstance(){
		if(minaTrans == null){
			synchronized (MinaTrans.class) {
				if(minaTrans == null){					
					minaTrans = new MinaTrans();
				}
			}
		}
		return minaTrans;
	}

	/**
	 * 产生事物
	 * @param sequenceId
	 * @param transInfo
     * @return
     */
	public MinaTrans post(Integer sequenceId, TransactionInfo transInfo){
		mTransMap.put(sequenceId, transInfo);
		return minaTrans;
	}
	
	/**
	 * @return 返回递增的Id
	 */
	public static Integer getCurrentSequenceId(){
		return mTransMap.size()+1;
	}

	/**
	 * 发送请求
	 * @param sequenceId
     */
	public void deliver(Integer sequenceId){
		mTransMap.get(sequenceId).getCallback().execute();
	}

	/**
	 * 请求回调
	 * @param sequenceId
	 * @param msg
     * @return
     */
	public static boolean handleTrans(Integer sequenceId, Object msg){
		
		if(sequenceId.intValue() <= 0) return false;
		
		TransactionInfo transactionInfo = mTransMap.remove(sequenceId);
		
		if(null != transactionInfo && null != transactionInfo.getCallback()){
			transactionInfo.getCallback().callback(msg);
			return true;
		}
		
		return false;
	}
	
}
