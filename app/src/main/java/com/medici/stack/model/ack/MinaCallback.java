package com.medici.stack.model.ack;

/**
 * @Desc 自定义回调机制的接口
 * @author 李宗好
 * @time:2017年2月20日下午4:05:37
 */
public interface MinaCallback<T> {
	/**
	 * 通信成功的返回
	 * @param data 数据
	 */
	void onSucceed(T data);

	/**
	 * 超时返回
	 */
	void timeOut();

	/**
	 * 执行事物
	 * @param sequenceId 序列Id
	 */
	void execute(long sequenceId);
}
