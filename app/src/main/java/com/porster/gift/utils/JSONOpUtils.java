package com.porster.gift.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class JSONOpUtils {
	public static String dateFormat="yyyy-MM-dd HH:mm:ss";
	/** 返回数据格式错误 --value:-500*/
	public final static int DATA_ERROR=-500;
	
	
	/**
	 * 将String对象转换成JSONObject对象
	 * @param jsonData
	 * @return
	 */
	public static JSONObject getJSONObject(String jsonData){
		if(jsonData==null) return null;
		JSONObject jobj=null;
		try {
			jobj = new JSONObject(jsonData);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj;
	}
	
	public static JSONObject getJSONObject(JSONObject src,String key){
		if(src==null || src.isNull(key)) return null;
		JSONObject jobj=null;
		try {
			jobj = src.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jobj;
	}
	
	public static int getIntData(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return DATA_ERROR;
		try{
			return jobj.getInt(key);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getIntData",ex);
		}
		return DATA_ERROR;
	}
	
	public static long getLongData(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return DATA_ERROR;
		try{
			return jobj.getLong(key);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getLongData",ex);
		}
		return DATA_ERROR;
	}
	
	public static boolean getBoleanData(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return false;
		try{
			return jobj.getBoolean(key);
		}catch (Exception ex) {
			LogCat.e("JSONOpUtils","getBoleanData",ex);
		}
		return false;
	}
	
	public static JSONArray getJSONArray(String str){
		if(str==null) return null;
		try{
			return new JSONArray(str);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getJSONArray",ex);
		}
		return null;
	}
	
	public static JSONArray getJSONArray(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return null;
		try{
			return jobj.getJSONArray(key);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getJSONArray",ex);
		}
		return null;
	}
	/**
	 * 从JSONArray中获取JSONObject对象
	 * @param jarr 源JSONArray
	 * @param index 对象索引
	 * @return 异常返回null
	 */
	public static JSONObject getJSONObject(JSONArray jarr,int index){
		if(jarr==null) return null;
		try{
			return jarr.getJSONObject(index);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getJSONObject",ex);
			return null;
		}
	}
	
	/**
	 * 从JSONArray中获取String对象
	 * @param jarr 源JSONArray
	 * @param index 对象索引
	 * @return 异常返回null
	 */
	public static Object getObject(JSONArray jarr,int index){
		if(jarr==null) return null;
		try{
			return jarr.get(index);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getObject",ex);
			return null;
		}
	}
	
	/***
	 * Java 对像转换成 JSONObject
	 * @param entityName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static JSONObject object2Json(Object entityName){
		JSONObject jsonObj=null;
		try {
			jsonObj = new JSONObject();
			Field fields[] = entityName.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);// 设置私有属性可用
				Class c = field.getType();
				Object obj = field.get(entityName);
				//System.out.println(obj.toString());
				String key = field.getName();
				if (TypeValidation.isString(c)) {
					String str = (String) obj;
					jsonObj.put(key, str);
				} else if (TypeValidation.isInteger(c)) {
					int v = (Integer) obj;
					jsonObj.put(key, v);
				} else if (TypeValidation.isLong(c)) {
					long v = (Long) obj;
					jsonObj.put(key, v);
				} else if (TypeValidation.isBoolean(c)) {
					boolean b = (Boolean) obj;
					jsonObj.put(key, b);
				} else {
					jsonObj.put(key, obj);
				}
			}
		}catch(Exception e){
			LogCat.e("JSONOpUtils", e.getMessage());
		}
		return jsonObj;
	}
	
	
	
	/**
	 * 从JSONArray中获取String对象
	 * @param jarr 源JSONArray
	 * @param index 对象索引
	 * @return 异常返回null
	 */
	public static <T> Object getObject(JSONArray jarr,int index,Class<T> clazz){
		if(jarr==null) return null;
		try{
			if(TypeValidation.isInteger(clazz)||TypeValidation.isNumber(clazz)){
				return jarr.getInt(index);
			}else if(TypeValidation.isString(clazz)){
				return jarr.getString(index);
			}else if(TypeValidation.isBoolean(clazz)){
				return jarr.getBoolean(index);
			}else if(TypeValidation.isLong(clazz)){
				return jarr.getLong(index);
			}else if(TypeValidation.isDouble(clazz)){
				return jarr.getDouble(index);
			}
			return jarr.get(index);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","getObject",ex);
			return null;
		}
	}
	
	/**
	 * 向JSONArray中插入JSONObject对象
	 * @param jarr 目标JSONArray
	 * @param jobj 待插入的数据对象
	 * @param index 插入的位置
	 * @return 异常返回false
	 */
	public static boolean insertJSONObject(JSONArray jarr,JSONObject jobj,int index){
		if(jobj==null||jarr==null) return false;
		try{
			jarr.put(index,jobj);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","insertJSONObject",ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 向JSONArray中插入Object对象
	 * @param jarr 目标JSONArray
	 * @param obj 待插入的数据对象
	 * @return 异常返回false
	 */
	public static boolean putObjectTOJsonArray(JSONArray jarr,Object obj){
		if(obj==null||jarr==null) return false;
		try{
			jarr.put(obj);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","putObjectTOJsonArray",ex);
			return false;
		}
		return true;
	}

	/**
	 * 从jobj中确定键值为key的数据值
	 * @param jobj
	 * @param key
	 * @return 异常或没有结果返回null
	 */
	public static Object getObject(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return null;
		Object result=null;
		try{
			result=jobj.get(key);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","jsonGetObject",ex);
		}
		return result;
	}
	
	public static String getString(JSONObject jobj,String key){
		if(jobj==null || jobj.isNull(key)) return null;
		String result=null;
		try{
			result=jobj.getString(key);
		}catch(Exception ex){
			LogCat.e("JSONOpUtils","jsonGetString",ex);
		}
		return result;
	}
	
	public static boolean addObjToJSONObject(JSONObject jobj,String key,Object obj){
		if(jobj==null||obj==null) return false;
		try {
			jobj.put(key, obj);
		} catch (Exception e) {
			LogCat.e("JSONOpUtils","addObjToJSONObject",e);
			return false;
		}
		return true;
	}
	
	/**
	 * 根据属性名查找属性字段
	 * @param clazz
	 * @param fieldName 属性名
	 * @return 如果找到返回该属性
	 */
	public static Field findField(Class<?> clazz,String fieldName){
		if(clazz==null || fieldName==null) return null;
		Field field=null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
		}
		if (field == null) { //没找到
			Class<?> superClazz=clazz.getSuperclass();
			if(superClazz!=null){
				return findField(superClazz, fieldName);
			}
		}
		return field;
	}
	
	/**
	 * 根据属性名查找属性的set方法
	 * @param clazz
	 * @param fieldName 属性名
	 * @param parameterTypes 参数类型
	 * @return 如果找到返回该方法，否则返回null
	 */
	public static Method findFieldSetMethod(Class<?> clazz,String fieldName,Class<?>...parameterTypes){
		if (fieldName==null || fieldName.length()<1) {
			return null;
		}
		String firstC=fieldName.substring(0,1);
		firstC=firstC.toUpperCase(Locale.getDefault());
		String strMethod="set"+firstC+fieldName.substring(1, fieldName.length());
		Method method=null;
		try {
			method = clazz.getDeclaredMethod(strMethod,parameterTypes);//只找当前类的方法
		} catch (Exception e) {
		}
		try {
			method = clazz.getMethod(strMethod, parameterTypes);//只找父类公共的方法
		} catch (Exception e) {
		}
		//没按规范写set方法
		if (method==null) {
			strMethod="set"+fieldName;
		}
		try {
			method = clazz.getDeclaredMethod(strMethod, parameterTypes);//只找当前类的方法
		} catch (Exception e) {
		}
		try {
			method = clazz.getMethod(strMethod,parameterTypes);//只找父类公共的方法
		} catch (Exception e) {
		}
		return method;
	}
	
	/**
	 * 本方法只适用于将JSONObject对象转换成自定义的bean类型，并且自动装载自定义的bean中的Bean属性
	 * 只支持基本类型、自定义bean,不支持数组、Collection、Map、java.util.Date等类型
	 * @param <T> 自定义的bean类型,如果bean中有List变量，则需自实现set方法，参数为JSONArray
	 * @param jobj JSONObject对象
	 * @param clazz 自定义的bean类型的类
	 * @return 返回转换后的bean实例对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception 
	 */
	public static <T> T jsonToBean(JSONObject jobj,Class<T> clazz ) throws Exception{
		if(jobj==null) return null;
		if(clazz.isPrimitive()|| TypeValidation.isNumber(clazz)|| TypeValidation.isBoolean(clazz)
				|| TypeValidation.isString(clazz)||TypeValidation.isArray(clazz)||TypeValidation.isMap(clazz)
				||Date.class.isAssignableFrom(clazz)){
			throw new UnsupportedTypeException(clazz+"对象类型为不支持的转换类型!");
		}
		Iterator<?> keys=jobj.keys();
		if (!keys.hasNext()) {
			return null;
		}
		T result=clazz.newInstance();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (key == null || key.equals("") ){
				continue;
			}
			Field field = findField(clazz,key);
			if (field == null) {
				LogCat.v("JSONOpUtils jsonToBean", "在Bean" + clazz+ "未定义属性" + key);
				continue;
			}
//			Logger.i(field.getType().asSubclass(ObservableField.class).toString());
			Class<?> fieldType =null;
			try {
				field.setAccessible(true);
				fieldType = field.getType();
			} catch (Exception e) {
			}
			if(jobj.isNull(key)){
				if (TypeValidation.isString(fieldType)) {
					field.set(result, "");
				}
				continue;
			}
			
			if (fieldType==null) {
				continue;
			}
			Object value = getObject(jobj, key);
			if (value==null||value==JSONObject.NULL) { 
				if (TypeValidation.isString(fieldType)) {
					field.set(result, "");
				}
				continue;
			}
			
			try {
				if(TypeValidation.isInteger(fieldType)){
					if(value instanceof String){
						field.set(result, strToInt(value.toString()));
					} else if ((value instanceof Double)) {
						double dvalue=(Double)value;
						field.set(result, (int)dvalue);
					} else{
						field.set(result, value);
					}
				}else if(TypeValidation.isBoolean(fieldType)){
					if(value instanceof String){
						field.set(result, strToBoolean(value.toString()));
					}else{
						field.set(result, value);
					}
				}else if(TypeValidation.isLong(fieldType)){
					if(value instanceof String){
						field.set(result, strToLong(value.toString()));
					}else{
						field.set(result, value);
					}
				}else if (TypeValidation.isString(fieldType)) {
					field.set(result,value.toString());
				} else if (fieldType.isPrimitive() || TypeValidation.isNumber(fieldType)
						|| TypeValidation.isBoolean(fieldType)|| TypeValidation.isString(fieldType)) {
					try {
						field.set(result, value);
					} catch (Exception e) {
						//System.out.println("value type:"+value.getClass()+" ,value:"+value);
					}
					
				} else if (TypeValidation.isArray(fieldType)) { // 需提供set方法bean中自处理
					if (value instanceof JSONArray) {
						Method method=findFieldSetMethod(clazz, key,JSONArray.class);
						if (method==null) {
							continue;
						}
						method.setAccessible(true);
						method.invoke(result, value);
					}
					continue;
				} else if (TypeValidation.isMap(fieldType)) {
					if (value instanceof JSONObject) {
						Method method=findFieldSetMethod(clazz, key,JSONObject.class);
						if (method==null) {
							try {
								Map<String, Object> map=jsonObjectToMap((JSONObject)value);
								field.set(result, map);
							} catch (Exception e) {
								LogCat.e("JSONOpUtils","set map value",e);
							}
						}else {
							method.setAccessible(true);
							method.invoke(result, value);
						}
					}
					continue;
				}
//				else if (Date.class.isAssignableFrom(fieldType)) {
//					if(value!=null){
//						String strDate=value.toString();
//						Date dvalue=DateFormatUtils.formatStrToDate(strDate, dateFormat);
//						field.set(result, dvalue);
//					}
//					continue;
//				}
//				else if(Result.class.isAssignableFrom(fieldType)||PageResult.class.isAssignableFrom(fieldType)){
//					continue;
//				}
				else { // bean处理
					JSONObject jvalue = getJSONObject(jobj, key);
					Object res = jsonToBean(jvalue, fieldType);
					field.set(result, res);
				}
			} catch (Exception e) {
				LogCat.e("error field:"+field.getName()+",value:"+value);
				throw new Exception("error field:"+field.getName(), e);
			}
		}
		return result;
	}
	
	private static Map<String,Object> jsonObjectToMap(JSONObject jsonObj){
		Iterator<String> it = jsonObj.keys();
		if (!it.hasNext()) {
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		while(it.hasNext()){
			String key = it.next();
			Object value = getObject(jsonObj, key);
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * 本方法只适用于将JSONArray转换成一个List中存放基本类型、自定义Bean类型的转换
	 * 并且该bean中的属性为基本数据类型、自定义bean类型
	 * @param <T>
	 * @param jarr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> ArrayList<T> jsonToList(JSONArray jarr, Class<T> clazz)throws UnsupportedTypeException{
		if (jarr == null)
			return null;
		int size = jarr.length();
		ArrayList<T> list = new ArrayList<T>();

		if (clazz.isPrimitive() || TypeValidation.isNumber(clazz)
				|| TypeValidation.isBoolean(clazz) || TypeValidation.isString(clazz)) {
			for (int i = 0; i < size; i++) {
				@SuppressWarnings("unchecked")
				T value = (T)getObject(jarr, i, clazz);
				if (value == null) {
					continue;
				}
				list.add(value);
			}
		} else if (TypeValidation.isArray(clazz)|| TypeValidation.isJSONArray(clazz) || TypeValidation.isMap(clazz)) { 
			// 数组、JSON数组、集合、map暂不处理
			throw new UnsupportedTypeException(clazz+"对象类型为不支持的转换类型!");
		} else if (Date.class.isAssignableFrom(clazz)) {
			throw new UnsupportedTypeException(clazz+"对象类型为不支持的转换类型!");
		} else { // bean处理
			for (int i = 0; i < size; i++) {
				JSONObject jobj=getJSONObject(jarr, i);
				try{
					T value=jsonToBean(jobj, clazz);
					list.add(value);
				}catch (Exception e) {
					LogCat.e("JSONOpUtils", "jsonToList", e);
				}
			}
		}

		return list;
	}
	
	public static int strToInt(String str){
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static long strToLong(String str){
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return -1L;
		}
	}
	
	public static boolean strToBoolean(String str){
		try {
			return Boolean.parseBoolean(str);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 将json字符串转为Map
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Map<String,Object> sonToMap(String json) throws JSONException{
		JSONObject jsonObj = new JSONObject(json);
		return sonObjectToMap(jsonObj);
	}
	
	/**
	 * 将json字符串转为List
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static List<Map<String,Object>> sonToList(String json) throws JSONException{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		JSONArray array = new JSONArray(json);
		for(int i=0;i<array.length();i++){
			list.add(sonObjectToMap(array.getJSONObject(i)));
		}
		return list;
	}
	
	private static Map<String,Object> sonObjectToMap(JSONObject jsonObj) throws JSONException{
		Map<String,Object> map = new HashMap<String,Object>();
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			String value = jsonObj.getString(key);
			if(value != null && value.length() > 0){
				if( value.charAt(0) == '{'){
					map.put(key, sonToMap(value));
				}else if( value.charAt(0) == '[' ){
					map.put(key, sonToList(value));
				}else{
					map.put(key, value);
				}
			}
		}
		return map;
	}
	
	/**
	 * 从Map中获取值 <br>如：getValueFromMap(map,"fridends","[0]","name")
	 * @param map
	 * @param keys
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static Object getValueFromMap(Map<String,Object> map,String... keys) throws JSONException{
		List<String> usedKeyList = new ArrayList<String>();
		Object current = map;
		for(String key:keys){
			if(current == null){
				StringBuilder errorMsg = new StringBuilder("root");
				for(String usedKey:usedKeyList){
					errorMsg.append(" ==> ").append(usedKey);
				}
				errorMsg.append(" not exist");
				throw new JSONException(errorMsg.toString());
			}else if(current instanceof Map){
				current = ( (Map<String,Object>)current ).get(key);
			}else if(current instanceof List){
				int index = splitIndex(key);
				current = ( (List<Map<String,Object>>) current ).get(index);
			}else{
				StringBuilder errorMsg = new StringBuilder("root");
				for(String usedKey:usedKeyList){
					errorMsg.append(" ==> ").append(usedKey);
				}
				errorMsg.append(" don't have subitem");
				throw new JSONException(errorMsg.toString());
			}
			usedKeyList.add(key);
		}
		return current;
	}
	
	/**
	 * 拆分出索引
	 * @param string
	 * @return
	 */
	private static int splitIndex(String string){
		Pattern p = Pattern.compile("\\[\\d+\\]");
		Matcher m = p.matcher(string);
		m.find();
		StringBuilder indexString = new StringBuilder(m.group(0));
		indexString.deleteCharAt(0);
		indexString.deleteCharAt(indexString.length()-1);
		return Integer.valueOf(indexString.toString());
	}


}
