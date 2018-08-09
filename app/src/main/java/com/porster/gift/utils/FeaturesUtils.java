package com.porster.gift.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class FeaturesUtils {
	
	
	
	/**
	 * 获取最顶层的父级Activity
	 * @param activity 当前activity对象
	 * @return 没有父级Activity则返回当前activity
	 */
	public static Activity getTopActivity(Activity activity){
		Activity topActivity=activity.getParent();
		Activity result=topActivity;
		while(topActivity!=null){
			topActivity=topActivity.getParent();
			if(topActivity!=null){
				result=topActivity;
			}
		}
		if(result==null){
			result=activity;
		}
		return result;
	}
	
	/**
	 * 将assets下的所有文件复制到sd卡中
	 * @param context
	 * @param filePath
	 * @return 失败返回null,成功返回保存到sd卡中的路径
	 */
	public static String moveHtmlDataToSdcard(Context context,String filePath){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return null;
		}
		String root=Environment.getExternalStorageDirectory().getPath();
		String path=root+"/"+filePath;
		try {
			if(copyAssets(context, "", path)){
				return path;
			}
		} catch (Exception e) {
			Log.e("FeaturesUtils","moveHtmlDataToSdcard error",e);
		}
		return null;
	}
	
	public static boolean copyAssetsFile(Context context,File outFile,String assetsName) {
		InputStream in = null;
		OutputStream out = null;
		try {	
			if (outFile.exists()) outFile.delete();
			in = context.getResources().getAssets().open(assetsName);
			out = new FileOutputStream(outFile);

			int CACHESIZE=8*1024;
			byte[] buf = new byte[1024];
			int len;
			int tempLen=0;
			while ((len = in.read(buf)) > 0) {
				tempLen+=len;
				out.write(buf, 0, len);
				if(tempLen>=CACHESIZE){
					out.flush();
				}
			}
			out.flush();
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","copyAssetsFile error",e);
			return false;
		} catch (IOException e) {
			Log.e("FeaturesUtils","copyAssetsFile error",e);
			return false;
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (Exception e2) {
				}
			}
			if(out!=null){
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}
		return true;
	}
	
	/**
	 * 拷贝assets下的文件到sd卡中
	 * @param context
	 * @param assetDir
	 * @param dir
	 * @return
	 * @throws IOException
	 */
	public static boolean copyAssets(Context context, String assetDir, String dir)
			throws IOException {
		String[] files = context.getResources().getAssets().list(assetDir);
		File mWorkingPath = new File(dir);
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {
				return false;
			}
		}

		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			//sounds，webkit，images这三个文件夹是系统自带的，不用拷贝,config.properties是配置文件
			if(assetDir.length()==0 && (fileName.equalsIgnoreCase("sounds") || fileName.equalsIgnoreCase("webkit")
					|| fileName.equalsIgnoreCase("images") || fileName.equals("config.properties"))){
				continue;
			}else if (fileName.endsWith(".db")) {
				continue;
			}else if (!fileName.contains(".")) { //这里暂时用有没有后缀判定是否文件夹，因为不知道其他方法
				if (0 == assetDir.length()) {
					if(!copyAssets(context, fileName, dir + fileName + "/")){
						return false;
					}
				} else {
					if(!copyAssets(context, assetDir + "/" + fileName, dir+fileName+"/")){
						return false;
					}
				}
				continue;
			}
			String assetsName=null;
			if (0 != assetDir.length())
				assetsName=assetDir + "/" + fileName;
			else
				assetsName=fileName;
			File outFile = new File(mWorkingPath, fileName);
			copyAssetsFile(context, outFile, assetsName);
		}
		return true;
	}
	
	
	/**
	 * 删除文件
	 * @param filename
	 * @return
	 */
	public static boolean deleteFile(String filename){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return false;
		}
		String root=Environment.getExternalStorageDirectory().getPath()+"/";
		if(!filename.startsWith(root)){
			if(filename.startsWith("/")){
				filename=filename.substring(1);
			}
			filename=root+filename;
		}
		
		File file=new File(filename);
		if(file.exists()){
			return file.delete();
		}
		return true;
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @param filename
	 * @return
	 */
	public static boolean deleteFile(String path,String filename){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return false;
		}
		String root=Environment.getExternalStorageDirectory().getPath();
		if(!path.endsWith("/")){
			path=path+"/";
		}
		if(!path.startsWith(root)){
			if(path.startsWith("/")){
				path=path.substring(1);
			}
			path=root+"/"+path;
		}
		if(filename.startsWith("/")){
			filename=filename.substring(1);
		}
		filename=path+"/"+filename;
		Log.v("deletePath","delete file:"+filename);
		File file=new File(filename);
		if(file.exists()){
			return file.delete();
		}
		return true;
	}
	
	/**
	 * 删除文件
	 * @param path 待删除的路径
	 * @param deleteRoot 是否删除当前目录
	 * @return
	 */
	public static boolean deletePath(String path,boolean deleteRoot){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return false;
		}
		String root=Environment.getExternalStorageDirectory().getPath();
		if(!path.endsWith("/")){
			path=path+"/";
		}
		if(!path.startsWith(root)){
			if(path.startsWith("/")){
				path=path.substring(1);
			}
			path=root+"/"+path;
		}
		File file=new File(path);
		delete(file,deleteRoot);
		return true;
	}
	
	private static void delete(File file,boolean deleteRoot){
		if(file.exists()){
			if(file.isDirectory()){
				File[] files=file.listFiles();
				for(File tmp:files){
					delete(tmp,true);
				}
				if(deleteRoot) file.delete();
			}else{
				Log.v("deletePath","delete file:"+file.getName());
				file.delete();
			}
		}
	}
	
	
	/** 删除手机内存中的文件*/
	public static boolean deleteFile(Context context,String filename){
		File file=context.getFileStreamPath(filename);
		if(!file.exists()){
			return true;
		}else{
			return file.delete();
		}
		
	}
	
	public static String readFileToStr(String fileName,String encoding){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return null;
		}
		File file=new File(fileName);
		if(!file.exists()){
			return null;
		}
		int BUFSIZE=8*1024;
		FileInputStream istream=null;
		try {
			istream  =   new  FileInputStream(file);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte buf[]=new byte[BUFSIZE];
			int readLen=0;
			while ((readLen=istream.read(buf))!=-1) {
				bos.write(buf,0,readLen);
			}
			bos.flush();
			byte[] data=bos.toByteArray();
			bos.close();
			return new String(data,encoding);
		} catch (FileNotFoundException e) {
			Log.e("FeatureesUtils","readFileToStr",e);
		} catch (Exception e) {
			Log.e("FeatureesUtils","readFileToStr",e);
		}finally{
			if(istream!=null){
				try {
					istream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	/**
	 * 将序列化对象保存到sd卡文件
	 * @param <T> Serializable的实现接口
	 * @param filename 要保存到文件的目录和文件名,不需要包含sd卡根目录
	 * @param obj 要保存的序列化对象
	 * @return 成功返回true,异常或失败返回false
	 */
	public static <T extends Serializable> boolean saveSerializable(String path,String filename,T obj){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return false;
		}
		String root=Environment.getExternalStorageDirectory().getPath()+"/"+path;
		File file=new File(root);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		if(!filename.startsWith("/")){
			filename="/"+filename;
		}
		file=new File(root+filename);
		if(!file.getParentFile().exists()){
			if(!file.getParentFile().mkdirs()){
				return false;
			}
		}
		ObjectOutputStream out=null;
		try {
			FileOutputStream outstream = new FileOutputStream(file);
			out = new ObjectOutputStream(outstream);
			out.writeObject(obj); //将这个对象写入流
			out.flush();// 清空
			return true;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch(Exception e){
			Log.e("FeaturesUtils","saveSerializable error",e);
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}
		return false;
	}
	
	/**
	 * 将序列化对象保存到sd卡文件
	 * @param <T> Serializable的实现接口
	 * @param filename 要保存到文件的目录和文件名,不需要包含sd卡根目录
	 * @param obj 要保存的序列化对象
	 * @return 成功返回true,异常或失败返回false
	 */
	public static <T extends Serializable> boolean saveSerializable(String filename,T obj){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return false;
		}
		String root=Environment.getExternalStorageDirectory().getPath();
		if(!filename.startsWith("/")){
			filename="/"+filename;
		}
		File file=new File(root+filename);
		if(!file.getParentFile().exists()){
			if(!file.getParentFile().mkdirs()){
				return false;
			}
		}
		ObjectOutputStream out=null;
		try {
			FileOutputStream outstream = new FileOutputStream(file);
			out = new ObjectOutputStream(outstream);
			out.writeObject(obj); //将这个对象写入流
			out.flush();// 清空
			return true;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch(Exception e){
			Log.e("FeaturesUtils","saveSerializable error",e);
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}
		return false;
	}
	
	/**
	 * 读取sd卡序列化对象文件
	 * @param <T> 序列化对象类型
	 * @param filename 文件目录和文件名,不需要包含sd卡根目录
	 * @param needDelete 读完后是否删除文件
	 * @return 读取成功返回该对象，否则返回null
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public static <T extends Serializable> T readSerializable(String filename,boolean needDelete){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//sd卡存储可写
			return null;
		}
		String root=Environment.getExternalStorageDirectory().getPath();
		if(!filename.startsWith("/")){
			filename="/"+filename;
		}
		File file=new File(root+filename);
		if(!file.exists()){
			return null;
		}
		ObjectInputStream oin  = null;
		/*  打开文件并设置成从中读取对象。  */  
        try {
			FileInputStream istream  =   new  FileInputStream(file);
			oin=new ObjectInputStream(istream);
	        /**/ /*  读取对象  */  
	        Object tmp=oin.readObject(); 
	       // System.out.println("jgkjkj:"+tmp);
	        T obj  =  (T)tmp;
	        return obj;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (OptionalDataException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (ClassNotFoundException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch(ClassCastException e){
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch(Exception e){
			Log.e("FeaturesUtils","readSerializable error",e);
		}finally{
			if(needDelete) file.delete();
		}

		return null;
	}
	
	/**
	 * 将序列化对象保存到手机内存
	 * @param <T> Serializable的实现接口
	 * @param context
	 * @param filename 要保存到文件的目录和文件名,不需要包含sd卡根目录
	 * @param obj 要保存的序列化对象
	 * @return 成功返回true,异常或失败返回false
	 */
	public static <T extends Serializable> boolean saveSerializable(Context context,String filename,T obj){
		if (obj==null) {//sd卡存储可写
			return false;
		}
		ObjectOutputStream out=null;
		try {
			FileOutputStream outstream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			out = new ObjectOutputStream(outstream);
			out.writeObject(obj); //将这个对象写入流
			out.flush();// 清空
			return true;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","saveSerializable error",e);
		} catch(Exception e){
			Log.e("FeaturesUtils","saveSerializable error",e);
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (Exception e2) {
				}
			}
		}
		return false;
	}
	
	/**
	 * 读取手机内存序列化对象文件
	 * @param <T> 序列化对象类型
	 * @param context
	 * @param filename 文件目录和文件名,不需要包含sd卡根目录
	 * @param needDelete 读完后是否删除文件
	 * @return 读取成功返回该对象，否则返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T readSerializable(Context context,String filename,boolean needDelete){
		File file=context.getFileStreamPath(filename);
		if(!file.exists()){
			return null;
		}
		ObjectInputStream oin  = null;
		/*  打开文件并设置成从中读取对象。  */  
        try {
			FileInputStream istream  =   context.openFileInput(filename);
			oin=new ObjectInputStream(istream);
	        /**/ /*  读取对象  */  
	        T obj  =  (T)oin.readObject(); 
	        return obj;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (OptionalDataException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (ClassNotFoundException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch(ClassCastException e){
			Log.e("FeaturesUtils","readSerializable error",e);
		} catch(Exception e){
			Log.e("FeaturesUtils","readSerializable error",e);
		}finally{
			if(needDelete) file.delete();
		}

		return null;
	}
	
	/**
	 * 判断服务是否启动
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isRunning(Context context,String serviceName)
	{
		ActivityManager myAM=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
		ArrayList<RunningServiceInfo> runningServices = (ArrayList<RunningServiceInfo>) myAM.getRunningServices(40);
		//获取最多40个当前正在运行的服务，放进ArrList里,以现在手机的处理能力，要是超过40个服务，估计已经卡死，所以不用考虑超过40个该怎么办
		for(int i = 0 ; i<runningServices.size();i++)//循环枚举对比
		{
			String className=runningServices.get(i).service.getClassName();
			if(className.equals(serviceName))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 计算当前页码
	 * @param count 当前记录数
	 * @param pagecount 每页条数
	 * @return 页数
	 */
	public static int countPage(int count,int pagecount){
		int page=count/pagecount;
		if(count%pagecount>0){
			page=page+1;
		}
		return page;
	}
	
	/**
	 * 拷贝文件
	 * @param srcFile 源文件
	 * @param toFile 目标文件
	 * @return
	 */
	public static boolean copyFile(File srcFile,File toFile){
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		InputStream in = null;
		OutputStream out = null;
		try {	
			if (toFile.exists()) toFile.delete();
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(toFile);
			int CACHESIZE=8*1024;
			byte[] buf = new byte[1024];
			int len;
			int tempLen=0;
			while ((len = in.read(buf)) > 0) {
				tempLen+=len;
				out.write(buf, 0, len);
				if(tempLen>=CACHESIZE){
					out.flush();
				}
			}
			out.flush();
			return true;
		} catch (FileNotFoundException e) {
			Log.e("FeaturesUtils","copyFile error",e);
		} catch (IOException e) {
			Log.e("FeaturesUtils","copyFile error",e);
		}finally{
			if(in!=null){
				try { in.close(); } catch (Exception e2) { }
			}
			if(out!=null){
				try { out.close(); } catch (Exception e2) { }
			}
		}
		return false;
	}
	
	/**
	 * 判断assets目录下是否存在文件filename
	 * @param context
	 * @param assetDir assets下的目录
	 * @param filename 文件名称
	 * @return
	 */
	public static boolean existsAssetsFile(Context context,String assetDir,String filename){
		try {
			String[] files = context.getResources().getAssets().list(assetDir);
			if (files!=null && files.length>0) {
				for (String file:files) {
					if (file.equals(filename)) {
						return true;
					}
				}
			}
		} catch (IOException e) {
			Log.e("FeaturesUtils","existsAssetsFile error",e);
		} catch (Exception e) {
			Log.e("FeaturesUtils","existsAssetsFile error",e);
		}
		
		return false;
	}
	//地球半径
	public static final long EARTH_RADIUS=6378137; 
	public static final double PI=3.141592653589793;
//	#define rad(d) d * PI / 180.0
	
	private static double rad(double d) {
		return d * PI / 180.0;
	}

	/**
	 * 根据两经纬度坐标（double值），计算两点间距离，单位为米
	 * @param lng1 经度1
	 * @param lat1 纬度1
	 * @param lng2 经度2
	 * @param lat2 纬度2
	 * @return 返回计算后的距离
	 */
	public static double getDistance(double lng1,double lat1,double lng2,double lat2){
	    double radLat1 = rad(lat1);
	    double radLat2 = rad(lat2);
	    double a = radLat1 - radLat2;
	    double b = rad(lng1) - rad(lng2);
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	    s = s * EARTH_RADIUS;
	    s = Math.round(s * 1000000) / 1000000;
	    return s;
	}
	
}
