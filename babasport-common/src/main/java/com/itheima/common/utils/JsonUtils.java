package com.itheima.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 对象与JSOn相互转换
 * @author lx
 *
 */
public class JsonUtils {

	//转换器类
	private static ObjectMapper om = new ObjectMapper();
	
	static{
		om.setSerializationInclusion(Include.NON_NULL);
	}
	//对象到JSON
	public static String objectToJson(Object obj){
		try {
			return om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	// json 到对象  
	public static <T> T jsonToObject(String value,Class<T> javaType){
		T t = null ;
		try {
			t = om.readValue(value, javaType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
	

	
	
}
