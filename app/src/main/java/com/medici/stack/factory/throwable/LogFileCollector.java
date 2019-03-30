package com.medici.stack.factory.throwable;

import android.content.Context;
import android.support.annotation.NonNull;

import com.medici.stack.util.blankj.IOUtil;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 将重要的日志信息有选择的存放到内部存储或者外部存储的某个Log文件中,当发生Crash时,也可以
 * 收集这个Log文件中的内容并上传到服务器,帮助问题的分析和定位。
 */
public class LogFileCollector {

    private Context mContext;

    public LogFileCollector(@NonNull Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 日志写入文件并返回
     */
    @NonNull
    public String collectLogFile(@NonNull String fileName, int numberOfLines){
        final List<String> resultBuffer = new ArrayList<>(numberOfLines);

        final BufferedReader reader = getReader(fileName);

        try {
            String line = reader.readLine();
            if(null != line){
                resultBuffer.add(line+"\n");
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeIO(reader);
        }

        return resultBuffer.toString();
    }

    /**
     * 返回一个输入通道
     * @param fileName
     * @return
     */
    @NonNull
    private static BufferedReader getReader(@NonNull String fileName){
        final FileInputStream inputStream;
        File file = new File(fileName);
        try {
            if(null != file && file.exists()){
                inputStream = new FileInputStream(file);
                return new BufferedReader(new InputStreamReader(inputStream));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        }

        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
    }

}
