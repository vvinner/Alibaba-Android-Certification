package com.porster.gift.utils;

/**
 * <P>
 * {@code Exception} 说明:UnsupportedTypeException 不支持的类型异常
 * </P>
 * @author xiaojing chen
 * @company 杭州乐导科技有限公司
 * @email chenyulong1986@163.com
 * @weibo http://weibo.com/chenyulong86
 * @date 2015年8月27日 上午10:35:45
 * 
 * @see JSONOpUtils
 */
public class UnsupportedTypeException extends Exception {
	private static final long serialVersionUID = 8657294971273174196L;

	public UnsupportedTypeException(String message) {
		super(message);
	}
	
	public UnsupportedTypeException(Throwable ex){
		super(ex);
	}
	
	public UnsupportedTypeException(String message,Throwable ex){
		super(message, ex);
	}
}
