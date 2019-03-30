package com.medici.stack.model.api;

import java.util.Map;

/**
 * ***************************************
 *
 * @desc: 解析Model 到map的接口
 *
 * ***************************************
 */
public interface ParseSerialize {

    /**
     * 通过class对象获取Field,装箱到Map中
     * @return Map对象
     */
    Map<String,Object> parseField();

}