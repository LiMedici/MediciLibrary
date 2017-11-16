package com.medici.stack.util.gson;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * @Desc Gson工具类 
 * @author 李宗好
 * @time:2017年3月8日上午9:07:26
 */
public class GsonUtil {

	/**
	 * 实现格式化的时间字符串转时间对象
	 */
	private static final String DATEFORMATER_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * json字符串转list或者map
	 * @param json
	 * @param typeToken
	 * @return
	 */
	public static <T> T objectFromJson(String json, TypeToken<T> typeToken) {

		json = jsonFormat1(json);

		Gson gson = new GsonBuilder()
				.registerTypeAdapter(typeToken.getType(), new ObjectTypeAdapter())
				.registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
				.registerTypeAdapter(int.class, new IntegerTypeAdapter(-1))
				.registerTypeAdapter(Double.class, new DoubleTypeAdapter())
				.registerTypeAdapter(double.class, new DoubleTypeAdapter(-1.00))
				.registerTypeAdapter(Long.class, new LongTypeAdapter())
				.registerTypeAdapter(long.class, new LongTypeAdapter(-1l))
				.create();

		return gson.fromJson(json, typeToken.getType());
	}

	/**
	 * 将一个javaBean生成对应的Json数据
	 * @param obj
	 * @param format 是否format
	 * @return
	 */
	public static String beanToJson(Object obj, boolean format) {

		Gson gson = buildGson(format);

		return gson.toJson(obj);
	}
	
	/**
	 * json字符串转bean对象
	 * @param json
	 * @param cls
	 * @param format 是否format
	 * @return
	 */
	public static <T> T jsonToBean(String json, Class<T> cls, boolean format) {

		Gson gson = buildGson(format);

		return gson.fromJson(json, cls);
	}

	/**
	 * json转换成Map 泛型在编译期类型被擦除导致的
	 * @param json
	 * @param clazz
	 * @param format
	 * @param <T>
     * @return Map集合
     */
	@Deprecated
	public static <T> Map jsonToMap(String json, Class<T> clazz, boolean format) {
		Gson gson = buildGson(format);
		Type type = new TypeToken<Map<String,T>>() {}.getType();
		Map<String,T> map = gson.fromJson(json,type);
		return map;
	}

	/**
	 * JSON转Map
	 * @param json
	 * @param clazz
	 * @param toEntity
	 * @param format
	 * @param <T>
     * @return
     */
	public static <T> Map jsonToMap(String json, Class<T> clazz, boolean toEntity, boolean format) {
		Gson gson = buildGson(format);
		Map<String,T> resultMap = Maps.newHashMap();
		Map<String,Object> map = objectFromJson(json,new TypeToken<Map<String,Object>>(){});
		for (Map.Entry<String,Object> entry:map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			String mapStr = beanToJson(value,false);
			T t = jsonToBean(mapStr,clazz,false);

			resultMap.put(key,t);
		}
		return resultMap;
	}

	/**
	 * 对象转实体
	 * @param object
	 * @param clazz
	 * @param format
	 * @param <T>
     * @return
     */
	public static <T> T objToEntity(Object object, Class<T> clazz, boolean format){
		String objectStr = beanToJson(object,format);
		T t = jsonToBean(objectStr,clazz,format);
		return t;
	}

	/**
	 * @param json
	 * @param clazz
	 * @param format 是否format
	 * @return ArrayList集合
	 */
	public static <T> ArrayList<T> jsonToList(String json, Class<T> clazz, boolean format) {
		Gson gson = buildGson(format);
		Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
		ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

		ArrayList<T> arrayList = new ArrayList<>();
		for (JsonObject jsonObject : jsonObjects) {
			arrayList.add(gson.fromJson(jsonObject,clazz));
		}
		return arrayList;
	}

	/**
	 * 对后台返回的数据进行检查 去掉json头尾的"字符串
	 * @param json
	 * @return
     */
	public static String jsonFormat1(String json){
		if(json.startsWith("\"")){
			json = json.substring(1);
		}

		if(json.endsWith("\"")){
			json = json.substring(0,json.length()-1);
		}

		return json;
	}

	/**
	 * 后台返回的数据进行检查 去掉json的\字符串
	 * @param json
	 * @return
     */
	public static String jsonFormat2(String json){
		if(json.contains("\\")){
			json = json.replace("\\","");
		}
		return json;
	}

	/**
	 * 返回gson对象
	 * @param format
	 * @return
     */
	private static Gson buildGson(boolean format){

		GsonBuilder gsonBuilder = new GsonBuilder();

		/**
		 * 设置默认时间格式
		 */
		gsonBuilder.setDateFormat(DATEFORMATER_DEFAULT)
				.registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
				.registerTypeAdapter(int.class, new IntegerTypeAdapter(-1))
				.registerTypeAdapter(Double.class, new DoubleTypeAdapter())
				.registerTypeAdapter(double.class, new DoubleTypeAdapter(-1.00))
				.registerTypeAdapter(Long.class, new LongTypeAdapter())
				.registerTypeAdapter(long.class, new LongTypeAdapter(-1l));

		/**
		 * 添加格式化设置
		 */
		if (format) {
			gsonBuilder.setPrettyPrinting();
		}

		return gsonBuilder.create();
	}
}
