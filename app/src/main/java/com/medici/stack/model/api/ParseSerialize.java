package com.medici.stack.model.api;

import java.util.Map;

/**
 * ***************************************
 *
 * @desc: 解析Model 到map的接口
 * @author：李宗好
 * @time: 2017/12/22 0022 09:53
 * @email：lzh@cnbisoft.com
 * @version：
 * @history:
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