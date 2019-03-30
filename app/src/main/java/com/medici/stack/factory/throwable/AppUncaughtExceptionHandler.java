package com.medici.stack.factory.throwable;

import com.medici.stack.util.blankj.IOUtil;
import com.orhanobut.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * desc:全局异常捕获处理器
 * author：李宗好
 * time: 2017/7/5 0005 15:03
 * email：lzh@cnbisoft.com
 */
public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        Throwable cause = throwable;

        while(null!=cause){
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        //CRASH堆栈信息
        final String stacktraceAsString = result.toString();

        Logger.e(stacktraceAsString);

        IOUtil.closeIO(printWriter);
        IOUtil.closeIO(result);
    }
}
