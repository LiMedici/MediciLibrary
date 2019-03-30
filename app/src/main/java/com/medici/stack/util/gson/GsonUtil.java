package com.medici.stack.util.gson;

import android.support.annotation.CheckResult;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc Google Json工具类
 */
public class GsonUtil {

    /**
     * 是否格式化日期
     */
    private static final boolean FORMAT = false;

    /**
     * 实现格式化的时间字符串转时间对象
     */
    private static final String FORMATTER_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();

        /**
         * 设置默认时间格式
         */
        gsonBuilder.setDateFormat(FORMATTER_DEFAULT)
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter(-1))
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter(-1.00))
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter(-1L))
                // 智能null
                .serializeNulls()
                // 调教模式
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                // json宽松
                .setLenient();

        /**
         * 添加格式化设置
         */
        if (FORMAT) {
            gsonBuilder.setPrettyPrinting();
        }

        gson = gsonBuilder.create();
    }

    /**
     * 返回可使用的Google Json对象
     *
     * @return Google Json
     */
    public static Gson gson() {
        return gson;
    }


    /**
     * 将一个javaBean生成对应的Json数据
     *
     * @param obj 对象
     * @return Json数据
     */
    public static String beanToJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * json字符串转bean对象
     *
     * @param json Json数据
     * @param clx 实体类Class对象
     * @return 泛型实体
     */
    public static <T> T jsonToBean(String json, Class<T> clx) {
        return gson.fromJson(json, clx);
    }

    /**
     * 对象转实体
     *
     * @param object 对象
     * @param clx 实体类Class对象
     * @return 泛型实体
     */
    public static <T> T objToBean(Object object, Class<T> clx) {
        String objectStr = beanToJson(object);
        T t = jsonToBean(objectStr, clx);
        return t;
    }

    /**
     * 带泛型的json转换
     *
     * @param json      json字符串
     * @param typeToken 要转换的Model的TypeToken对象
     * @param <T>       返回相应的Model
     * @return T 带泛型的数据
     */
    @CheckResult
    public static <T> T jsonToModel(String json, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        StringReader reader = new StringReader(json);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            T t = (T) adapter.read(jsonReader);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
