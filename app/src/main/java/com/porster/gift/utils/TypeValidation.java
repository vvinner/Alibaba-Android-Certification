package com.porster.gift.utils;

import org.json.JSONArray;

import java.util.Collection;
import java.util.Map;

/**
 * 类型验证工具类
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class TypeValidation {
	/**
	 * Tests if Class represents a Byte or a primitive Byte.<br>
	 */
	
	public static boolean isByte(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Byte.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		return Byte.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Short or a primitive Short.<br>
	 */
	public static boolean isShort(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Short.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		return Short.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Integer or a primitive Integer.<br>
	 */
	public static boolean isInteger(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Integer.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		return Integer.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Long or a primitive Long.<br>
	 */
	public static boolean isLong(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Long.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		
		return Long.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Float or a primitive Float.<br>
	 */
	public static boolean isFloat(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Float.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		return Float.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Double or a primitive Double.<br>
	 */
	public static boolean isDouble(Class clazz) {
		if (clazz == null) {
			return false;
		}
		if (Double.TYPE.isAssignableFrom(clazz)) {
			return true;
		}
		return Double.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if Class represents a Blob(byte array).<br>
	 */
	public static boolean isBlob(Class clazz) {
		return clazz != null && clazz.isArray() 
			&& Byte.TYPE.isAssignableFrom(clazz.getComponentType());
	}
	
	/**
	 * Tests if Class represents a primitive number or wrapper.<br>
	 */
	public static boolean isNumber(Class clazz) {
		return clazz != null && (Short.TYPE.isAssignableFrom(clazz)
				|| Integer.TYPE.isAssignableFrom(clazz)
				|| Long.TYPE.isAssignableFrom(clazz)
				|| Float.TYPE.isAssignableFrom(clazz)
				|| Double.TYPE.isAssignableFrom(clazz) 
				|| Number.class.isAssignableFrom(clazz));
	}
	
	/**
	 * Tests if Class represents a String or a char
	 */
	public static boolean isString(Class clazz) {
		return clazz != null && (String.class.isAssignableFrom(clazz) 
				|| (Character.TYPE.isAssignableFrom(clazz) 
				|| Character.class.isAssignableFrom(clazz)));
	}
	
	/**
	 * Tests if Class represents a Boolean or primitive boolean
	 */
	public static boolean isBoolean(Class clazz) {
		return clazz != null&& (Boolean.TYPE.isAssignableFrom(clazz) 
			|| Boolean.class.isAssignableFrom(clazz));
	}

	/**
	 * Tests if a Class represents an array or Collection.
	 */
	public static boolean isArray(Class clazz) {
		return clazz != null&& (clazz.isArray() 
				|| Collection.class.isAssignableFrom(clazz) 
				|| (JSONArray.class.isAssignableFrom(clazz)));
	}
	
	/**
	 * Tests if a Class represents an JSONArray.
	 */
	public static boolean isJSONArray(Class clazz){
		return clazz != null&& JSONArray.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Tests if a Class represents an map.
	 */
	public static boolean isMap(Class clazz) {
		return clazz != null&& (clazz.isArray() 
				|| Map.class.isAssignableFrom(clazz));
	}
}
