package com.medici.stack.model.minacallback;

/**
 * @Desc 自定义回调机制的接口
 * @author 李宗好
 * @time:2017年2月20日下午4:05:37
 */
public interface CmdCallback<T> {
	void callback(T msg);
	void timeOut();
	void execute();
}
