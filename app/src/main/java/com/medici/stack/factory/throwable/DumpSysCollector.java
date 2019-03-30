package com.medici.stack.factory.throwable;

import android.support.annotation.NonNull;

import com.medici.stack.util.blankj.IOUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Crash发生时候内存使用情况对某些类型的Crash定位也是有很大帮助的,
 * 通过执行dumpsys meminfo命令可以获取到当前进程的内存使用信息
 */
public class DumpSysCollector {

    private static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 1024 * 8;

    /**
     * 返回该进程的内存使用信息
     */
    @NonNull
    public static String collectMemInfo(){
        final StringBuilder memInfo = new StringBuilder();
        BufferedReader bufferedReader = null;

        final List<String> commandLine = new ArrayList<>();
        commandLine.add("dumpsys");
        commandLine.add("meminfo");
        commandLine.add(Integer.toString(android.os.Process.myPid()));

        try {
            final Process process = Runtime.getRuntime().exec(commandLine.toArray(new String[commandLine.size()]));

            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()),DEFAULT_BUFFER_SIZE_IN_BYTES);

            while(true){
                final String line = bufferedReader.readLine();
                if(null == line) break;
                memInfo.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeIO(bufferedReader);
        }

        return memInfo.toString();
    }

}
