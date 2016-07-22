package com.bbd.saas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String加密与解密
 * 
 * @author luobotao Date: 2015年4月14日 下午5:44:40
 */
public class StringUtil {
	public static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	public static String filterString(String str) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	/**
	 * 字符串为空时替换称指定文字
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String initStr(String oldString,String newString){
		if (oldString == null || "".equals(oldString) || "null".equals(oldString) || "undefined".equals(oldString))
			return newString.trim();
		else
			return oldString.trim();
	}
}
