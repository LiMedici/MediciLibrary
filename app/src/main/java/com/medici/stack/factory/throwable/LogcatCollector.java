package com.medici.stack.factory.throwable;

import android.os.Build;
import android.support.annotation.Nullable;

import com.medici.stack.util.blankj.IOUtil;
import com.medici.stack.factory.manager.ThreadManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * desc:捕获Logcat日志的好处是可以清楚得知道Crash发生前后的上下文,对于准确定位Crash来说
 *      提供了更完备的信息
 * author：李宗好
 * time: 2017/7/7 0007 14:02
 * ic_email_icon：lzh@cnbisoft.com
 */
public class LogcatCollector {

    //保留Logcat输出中最后的行数
    private static final int DEFAULT_TALL_COUNT = 100;

    //缓冲字节大小
    private static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 1024 * 8;

    /**
     * @param bufferName logcat 前缀标题名字
     * @param logcatFilterByPid 是否过滤其它APP的PID
     * @param logcatArguments logcat指令参数
     * @return
     */
    public String collectLogcat(@Nullable String bufferName, boolean logcatFilterByPid, String[] logcatArguments){
        final int myPid = android.os.Process.myPid();

        String myPidStr = null;

        //只手机当前进程相关的Logcat信息
        if(logcatFilterByPid && myPid > 0){
            myPidStr = Integer.toString(myPid)+"):";
        }

        final List<String> commandLine = new ArrayList<String>();

        commandLine.add("logcat");

        if(bufferName != null){
            commandLine.add("-b");
            commandLine.add(bufferName);
        }

        //logcat的 "-t n" 参数是API Level 8 才引入的,对于之前的系统版本
        //需要做特殊处理模拟这种情况

        final int tailCount;

        final List<String> logcatArgumentsList = new ArrayList<String>(Arrays.asList(logcatArguments));

        final int tailIndex = logcatArgumentsList.indexOf("-t");

        if(tailIndex > -1 && tailIndex < logcatArgumentsList.size()){
            tailCount = Integer.parseInt(logcatArgumentsList.get(tailIndex+1));

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO){
                logcatArgumentsList.remove(tailIndex+1);
                logcatArgumentsList.remove(tailIndex);
                logcatArgumentsList.add("-d");
            }
        }else {
            tailCount = -1;
        }

        final List<String> logcatBuf = new ArrayList<>(tailCount > 0?tailCount:DEFAULT_TALL_COUNT);

        commandLine.addAll(logcatArgumentsList);

        BufferedReader bufferedReader = null;

        try {
            final Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));

            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()),DEFAULT_BUFFER_SIZE_IN_BYTES);
            ThreadManager.getShortPool().execute(()->{
                InputStream stderr = null;

                try {
                    stderr = process.getErrorStream();

                    byte[] dummy = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];

                    while(stderr.read(dummy) >= 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    IOUtil.closeIO(stderr);
                }
            });

            while(true){
                final String line = bufferedReader.readLine();
                if(null == line) break;
                if(null == myPidStr || line.contains(myPidStr)){
                    logcatBuf.add(line+"\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeIO(bufferedReader);
        }

        return logcatBuf.toString();
    }

}
