package com.seasy.docker.admin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.seasy.docker.common.utils.StringUtil;

import net.sf.json.JSONObject;

public class JsonUtil {
    /**
     * 将json格式的字符串 转成 JSONObject对象
     */
    public static JSONObject string2object(String jsonData){
        if(StringUtil.isNotEmpty(jsonData)) {
            return JSONObject.fromObject(jsonData);
        }
        return null;
    }

    /**
     * 根据key从JSONObject对象获取其对应的值
     */
    public static String getString(JSONObject jsonObject, String key){
        if(jsonObject != null && jsonObject.containsKey(key)){
            String value = StringUtil.trimToEmpty(jsonObject.getString(key));
            return value;
        }
        return "";
    }

    public static String getString(JSONObject jsonObject, String key, String defaultValue){
        String value = defaultValue;
        if(jsonObject != null && jsonObject.containsKey(key)){
            value = StringUtil.trimToEmpty(jsonObject.getString(key));
        }
        return value;
    }

    /**
     * 以层次关系输出json字符串
     */
    public static String object2String(JSONObject object){
        if(object == null){
            return null;
        }else{
            return object.toString(2); //缩进两个空格
        }
    }

    /**
     * 将json格式字符串 转成 JavaBean对象
     */
    public static <T> T string2JavaBean(String jsonString, Class<T> clazz){
        //key转小写
        Matcher matcher = Pattern.compile("\"([A-Za-z0-9]+)\":").matcher(jsonString);
        while(matcher.find()){
            jsonString = jsonString.replace(matcher.group(), matcher.group().toLowerCase());
        }

        JSONObject object = JSONObject.fromObject(jsonString);
        T bean = (T)JSONObject.toBean(object, clazz);
        return bean;
    }

    public static String toJSONString(String key, String value){
        JSONObject object = new JSONObject();
        object.put(key, value);
        return object.toString(2);
    }

    public static String toJSONString(String key1, String value1, String key2, String value2){
        JSONObject object = new JSONObject();
        object.put(key1, value1);
        object.put(key2, value2);
        return object.toString(2);
    }

    public static String toJSONString(String key1, String value1, String key2, String value2, String key3, String value3){
        JSONObject object = new JSONObject();
        object.put(key1, value1);
        object.put(key2, value2);
        object.put(key3, value3);
        return object.toString(2);
    }

    public static String toJSONString(String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4){
        JSONObject object = new JSONObject();
        object.put(key1, value1);
        object.put(key2, value2);
        object.put(key3, value3);
        object.put(key4, value4);
        return object.toString(2);
    }

    public static String toJSONString(String key1, String value1, String key2, String value2, String key3, String value3, String key4, String value4, String key5, String value5){
        JSONObject object = new JSONObject();
        object.put(key1, value1);
        object.put(key2, value2);
        object.put(key3, value3);
        object.put(key4, value4);
        object.put(key5, value5);
        return object.toString(2);
    }
    
    public static void main(String[] args) {
		System.out.println(toJSONString("cjm", "123"));
		
		JSONObject object = new JSONObject();
		object.put("uid", "cjm");
		object.put("pwd", "123456");
		System.out.println(object2String(object));
		System.out.println(getString(object, "pwd"));
		
		object = string2object("{username:\"zhangsan\", password:\"123\"}");
		System.out.println(getString(object, "username"));
	}

}
