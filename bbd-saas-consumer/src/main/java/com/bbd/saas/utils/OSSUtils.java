package com.bbd.saas.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * OSS服务上传文件Util
 * @author luobotao
 * @Date 2015年6月19日
 */
public class OSSUtils {


	@Value("${configProperties['oss.access.id']}")
	private String ACCESS_ID ;
	@Value("${configProperties['oss.access.key']}")
	private String ACCESS_KEY ;
	@Value("${configProperties['oss.protocol']}")
	private String PROTOCOL;
	@Value("${configProperties['oss.endpoint']}")
	private String OSS_ENDPOINT;
	@Value("${configProperties['oss.bucket.name']}")
	private String BUCKET_NAME;
	/**
	 * @param file
	 * @param path 上传的目录 
	 * @param type 文件类型
	 * @param BUCKET_NAME BUCKET_NAME=Configuration.root().getString("oss.bucket.name.higouAPIDev", "higou-api");
	 * @return
	 * @throws OSSException
	 * @throws ClientException
	 * @throws FileNotFoundException
	 */
	public String uploadFile(File file, String path,String fileName, String type,String BUCKET_NAME){
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

	public String uploadFile(InputStream input, String path,String fileName, Long flength, String type,String BUCKET_NAME){
		if(!path.endsWith("/")){
            path = path + "/";
        }
		String endpoint = PROTOCOL + "://" + OSS_ENDPOINT + "/";
		OSSClient client = new OSSClient(endpoint, ACCESS_ID, ACCESS_KEY);
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(flength);
		objectMeta.setContentType(type);

		try {
			client.putObject(BUCKET_NAME, path + fileName+type, input, objectMeta);
		} catch (Exception e) {
			//e.printStackTrace();
			return "";
		}
//		return PROTOCOL + "://" + BUCKET_NAME + "." + OSS_ENDPOINT + "/" + path+ fileName;
		return "/" + path+ fileName+type;
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
	public String getOSSUrl(){
		return PROTOCOL + "://" + getBUCKET_NAME() + "." + OSS_ENDPOINT;
	}

	/**
	 * 上传目录下的所有文件
	 * @param dir
	 * @param BUCKET_NAME
	 */
	public void uploadAllFiles(File dir,String BUCKET_NAME) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			String path=files[i].getAbsolutePath();
				if (files[i].isDirectory()) {
					uploadAllFiles(files[i],BUCKET_NAME);
				} else {
					try {
						String fileName = files[i].getName();
						int p = fileName.lastIndexOf('.');
						String type = fileName.substring(p, fileName.length());
						String pathTemp = path.substring(0, path.length()).replaceAll("\\\\", "/").replaceAll(fileName, "");
						String url = uploadFile(files[i],pathTemp,fileName, type,BUCKET_NAME);
						System.out.println(url);
					} catch (Exception e) {
						e.printStackTrace();
					} 
			}
		}
	}


	public String getACCESS_ID() {
		return ACCESS_ID;
	}

	public void setACCESS_ID(String ACCESS_ID) {
		this.ACCESS_ID = ACCESS_ID;
	}

	public String getACCESS_KEY() {
		return ACCESS_KEY;
	}

	public void setACCESS_KEY(String ACCESS_KEY) {
		this.ACCESS_KEY = ACCESS_KEY;
	}

	public String getPROTOCOL() {
		return PROTOCOL;
	}

	public void setPROTOCOL(String PROTOCOL) {
		this.PROTOCOL = PROTOCOL;
	}

	public String getOSS_ENDPOINT() {
		return OSS_ENDPOINT;
	}

	public void setOSS_ENDPOINT(String OSS_ENDPOINT) {
		this.OSS_ENDPOINT = OSS_ENDPOINT;
	}

	public String getBUCKET_NAME() {
		return BUCKET_NAME;
	}

	public void setBUCKET_NAME(String BUCKET_NAME) {
		this.BUCKET_NAME = BUCKET_NAME;
	}

	public static void main(String[] args) {
//		String TMP_DIR = "G:/test/";//args[0];;
//		String BUCKET_NAME= "higou-api";//args[1];
//		System.out.println("文件所在目录为："+TMP_DIR+"============="+"bucket名称为："+BUCKET_NAME);
//		File file = new File(TMP_DIR);
//		uploadAllFiles(file,BUCKET_NAME);
//		System.out.println("上传完毕！");
//		System.out.println("上传完毕！");
//		System.out.println("上传完毕！");
	}
}
