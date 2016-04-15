package com.bbd.saas.utils;

public class NumberUtil {

	/**
	 * 数字为空时替换成指定数字
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final Integer defaultIfNull(Integer num,Integer defaultNum){
		if (num == null)
			return defaultNum;
		else
			return num;
	}
	
		
}
