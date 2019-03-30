package com.medici.stack.model.ack;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @desc 检查事物集合事物是否过时,如果过时就删除
 */
public class OutThread extends Thread {
	
	private Map<Long,TransactionInfo> mTransMap = null;
	
	public OutThread(Map<Long,TransactionInfo> mTransMap){
		this.mTransMap = mTransMap;
	}
	
	@Override
	public void run() {

		while(true){

			if(mTransMap != null && mTransMap.size() > 0){
				
				Iterator<Entry<Long, TransactionInfo>> it = mTransMap.entrySet().iterator();
				
				while(it.hasNext()) {

					Entry<Long, TransactionInfo> entry = it.next();

					// 获取事物
					TransactionInfo info = entry.getValue();

					if(info.getCreateTime() + info.getTimeOut() < System.currentTimeMillis()
							|| info.isDispose()){
						// 如果已经超时,就执行回调超时的方法,并且remove该事物。
						entry.getValue().getCallback().timeOut();
						mTransMap.remove(entry.getKey());
					}
				}
			}
		}
	}
	
}
