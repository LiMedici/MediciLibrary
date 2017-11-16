package com.medici.stack.model.minacallback;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Desc 检查事物集合事物是否过时,如果过时就删除
 * @author 李宗好
 * @time:2017年2月20日下午6:00:18
 */
public class OutThread extends Thread {
	
	private Map<Integer,TransactionInfo> mTransMap = null;
	
	public OutThread(Map<Integer,TransactionInfo> mTransMap){
		this.mTransMap = mTransMap;
	}
	
	@Override
	public void run() {
		while(true){
			if(mTransMap!=null && mTransMap.size() > 0){
				
				Iterator<Entry<Integer, TransactionInfo>> it = mTransMap.entrySet().iterator();
				
				while(it.hasNext()) {
					Entry<Integer, TransactionInfo> entry = it.next();
					//已经超时
					if(entry.getValue().getCreateTime() + entry.getValue().getTimeOut() < new Date().getTime()){
						entry.getValue().getCallback().timeOut();
						mTransMap.remove(entry.getKey());
					}
				}
			}
		}
	}
	
}
