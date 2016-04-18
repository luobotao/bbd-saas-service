package com.bbd.saas.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * OSS服务上传文件Util
 * @author luobotao
 * @Date 2015年6月19日
 */
public class OSSUtils {


	private static String PROTOCOL="http";
	private static String OSS_ENDPOINT="oss-cn-beijing.aliyuncs.com";
	/**
	 * @param file
	 * @param path 上传的目录 
	 * @param type 文件类型
	 * @param BUCKET_NAME
	 * @param ACCESS_ID
	 * @param ACCESS_KEY
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 */
	public static String uploadFile(File file, String path,String fileName, String type,String BUCKET_NAME,String ACCESS_ID,String ACCESS_KEY){
		if(!path.endsWith("/")){
            path = path + "/";
        }
		String endpoint = PROTOCOL + "://" + OSS_ENDPOINT + "/";
		OSSClient client = new OSSClient(endpoint, ACCESS_ID, ACCESS_KEY);
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(file.length());
		objectMeta.setContentType(type);
		InputStream input;
		try {
			input = new FileInputStream(file);
			client.putObject(BUCKET_NAME, path + fileName, input, objectMeta);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		return PROTOCOL + "://" + BUCKET_NAME + "." + OSS_ENDPOINT + "/" + path+ fileName;
		return "/" + path+ fileName;
	}

	/**
	 *
	 * @param input
	 * @param path
	 * @param fileName
	 * @param flength
	 * @param type
	 * @param BUCKET_NAME
	 * @param ACCESS_ID
     * @param ACCESS_KEY
     * @return
     */
	public static String uploadFile(InputStream input, String path,String fileName, Long flength, String type,String BUCKET_NAME,String ACCESS_ID,String ACCESS_KEY){
		if(!path.endsWith("/")){
            path = path + "/";
        }
		String endpoint = PROTOCOL + "://" + OSS_ENDPOINT + "/";
		OSSClient client = new OSSClient(endpoint, ACCESS_ID, ACCESS_KEY);
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(flength);
		objectMeta.setContentType(type);

		try {
			client.putObject(BUCKET_NAME, path + fileName, input, objectMeta);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
//		return PROTOCOL + "://" + BUCKET_NAME + "." + OSS_ENDPOINT + "/" + path+ fileName;
		return "/" + path+ fileName;
	}
	// 获取层级的方法
	public static String getLevel(int level) {
		// A mutable sequence of characters.
		StringBuilder sb = new StringBuilder();
		for (int l = 0; l < level; l++) {
			sb.append("|--");
		}
		return sb.toString();
	}

	public static void getAllFilesWithLevel(File dir, int level) {
		System.out.println(getLevel(level) + dir.getName());
		level++;
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				// 这里面用了递归的算法
				getAllFilesWithLevel(files[i], level);
			} else {
				System.out.println(getLevel(level) + files[i]);
			}
		}
	}
	
	/**
	 * 图片上传至OSS服务器，此方法返回OSS服务器的URL
	 * @return
	 */
	public static String getOSSUrl(String BUCKET_NAME){
		return PROTOCOL + "://" + BUCKET_NAME + "." + OSS_ENDPOINT;
	}


	/**
	 * 上传目录下的所有文件
	 * @param dir
	 * @param BUCKET_NAME
	 * @param ACCESS_ID
	 * @param ACCESS_KEY
     */
	public static void uploadAllFiles(File dir,String BUCKET_NAME,String ACCESS_ID,String ACCESS_KEY) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			String path=files[i].getAbsolutePath();
				if (files[i].isDirectory()) {
					uploadAllFiles(files[i],BUCKET_NAME,ACCESS_ID,ACCESS_KEY);
				} else {
					try {
						String fileName = files[i].getName();
						int p = fileName.lastIndexOf('.');
						String type = fileName.substring(p, fileName.length());
						String pathTemp = path.substring(0, path.length()).replaceAll("\\\\", "/").replaceAll(fileName, "");
						String url = uploadFile(files[i],pathTemp,fileName, type,BUCKET_NAME,ACCESS_ID,ACCESS_KEY);
					} catch (Exception e) {
						e.printStackTrace();
					} 
			}
		}
	}
}
