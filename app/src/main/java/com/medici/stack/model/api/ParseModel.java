package com.medici.stack.model.api;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
/**
 * ***************************************
 *
 * @desc: 实现解析Field成Map的接口,创建Map返回
 *
 * ***************************************
 */
public class ParseModel implements ParseSerialize{

    @Override
    public Map<String, Object> parseField() {
        Map<String,Object> map = new HashMap<>();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field:fields) {
                // 暴力反射
                field.setAccessible(true);
                Object fieldValue = field.get(this);
                map.put(field.getName(),fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }
}