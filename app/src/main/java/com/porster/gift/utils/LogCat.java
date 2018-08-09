package com.porster.gift.utils;

public  class LogCat {
	static final String TAG="LogCat";
	public static final String DEBUGGABLE_KEY="isDedbug";
	/**打印输出 : {@value}*/
	public static final String DEBUGGABLE_TRUE="true";
	/**禁止输出: {@value}*/
	public static final String DEBUGGABLE_FALSE="false";
	private static boolean isPrintLog;

	static {
		String debug=System.getProperty(DEBUGGABLE_KEY, DEBUGGABLE_FALSE);
		if(debug.equals(DEBUGGABLE_FALSE)){
			isPrintLog= false;
		}else{
			//isPrintLog=BuildConfig.DEBUG;
			isPrintLog=true;
		}
		System.out.println(">>>> debug: "+isPrintLog+" <<<<");
	}
	
	public static boolean isDebug(){
		return isPrintLog;
	}
	
	public static void setDebug(boolean isDebug){
		isPrintLog=isDebug;
	}

	public static int v(String msg){
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.v(ste.getClassName(),isEmpty(msg)?"null":msg);
		}
		return 0;
	}

	public static int i(String msg){
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.i(ste.getClassName(),isEmpty(msg)?"null":msg);
		}
		return 0;
	}

	public static int d(String msg){
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.d(ste.getClassName(),isEmpty(msg)?"null":msg);
		}
		return 0;
	}

	public static int e(String msg){
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.e(ste.getClassName(),isEmpty(msg)?"null":msg);
		}
		return 0;
	}

	public static int w(String msg){
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.w(ste.getClassName(),isEmpty(msg)?"null":msg);
		}
		return 0;
	}

	public static int v(String TAG,String msg){
		return isPrintLog?android.util.Log.v(TAG,isEmpty(msg)?"null":msg):0;
	}

	public static int e(String TAG,String msg){
		return isPrintLog?android.util.Log.e(TAG,isEmpty(msg)?"null":msg):0;
	}

	public static int w(String TAG,String msg){
		return isPrintLog?android.util.Log.w(TAG,isEmpty(msg)?"null":msg):0;
	}

	public static int i(String TAG,String msg){
		return isPrintLog?android.util.Log.i(TAG,isEmpty(msg)?"null":msg):0;
	}

	public static int d(String TAG,String msg){
		return isPrintLog?android.util.Log.d(TAG,isEmpty(msg)?"null":msg):0;
	}

	public static int e(String msg, Throwable tr) {
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.e(ste.getClassName(),isEmpty(msg)?"null":msg,tr);
		}
		return 0;
	}

	public static int w(String msg, Throwable tr) {
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.w(ste.getClassName(),isEmpty(msg)?"null":msg,tr);
		}
		return 0;
	}

	public static int v(String msg, Throwable tr) {
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.v(ste.getClassName(),isEmpty(msg)?"null":msg,tr);
		}
		return 0;
	}
	public static int i(String msg, Throwable tr) {
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.i(ste.getClassName(),isEmpty(msg)?"null":msg,tr);
		}
		return 0;
	}

	public static int d(String msg, Throwable tr) {
		if(isPrintLog){
			StackTraceElement[] stack = new Throwable().getStackTrace();
			StackTraceElement ste = stack[1];
			return android.util.Log.d(ste.getClassName(),isEmpty(msg)?"null":msg,tr);
		}
		return 0;
	}

	public static int e(String TAG, String msg, Throwable tr) {
		return isPrintLog?android.util.Log.e(TAG, isEmpty(msg)?"null":msg, tr):0;
	}

	public static int w(String TAG, String msg, Throwable tr) {
		return isPrintLog?android.util.Log.w(TAG, isEmpty(msg)?"null":msg, tr):0;
	}

	public static int v(String TAG, String msg, Throwable tr) {
		return isPrintLog?android.util.Log.v(TAG, isEmpty(msg)?"null":msg, tr):0;
	}
	public static int i(String TAG, String msg, Throwable tr) {
		return isPrintLog?android.util.Log.i(TAG, isEmpty(msg)?"null":msg, tr):0;
	}

	public static int d(String TAG, String msg, Throwable tr) {
		return isPrintLog?android.util.Log.d(TAG, isEmpty(msg)?"null":msg, tr):0;
	}

	/**
	 * @Description 不会禁止打印输出
	 * @Date 2013-9-25
	 * @param str
	 * @return void
	 */
	public static void println(String str){
		System.out.println(isEmpty(str)?"null":str);
	}
	
	static boolean isEmpty(String msg){
		return msg==null||msg.length()==0;
	}

}
