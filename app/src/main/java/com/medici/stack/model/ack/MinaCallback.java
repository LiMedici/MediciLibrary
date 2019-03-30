package com.medici.stack.model.ack;

/**
 * @desc 自定义回调机制的接口
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
