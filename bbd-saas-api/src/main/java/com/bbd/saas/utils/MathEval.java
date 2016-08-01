package com.bbd.saas.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据计算公式
 * 
 * @author 刘伟
 * 
 */
public class MathEval {

	public static char PLUS = '+';
	public static char MINUS = '-';
	public static char MULTI = '*';
	public static char DEVIDE = '/';

	public static char BRACKET_LEFT = '(';
	public static char BRACKET_RIGHT = ')';

	/**
	 * 只计算简单的两个数结果
	 * 
	 * @param op
	 * @param value1
	 * @param value2
	 * @return
	 */
	private static double simpleTwoEval(char op, double value1, double value2) {
		if (op == PLUS) {
			return value1 + value2;
		} else if (op == MINUS) {
			return value1 - value2;
		} else if (op == MULTI) {
			return value1 * value2;
		} else if (op == DEVIDE) {
			return value1 / value2;
		}
		return 0;
	}

	
	
	/**
	 * 只计算简单的两个数结果
	 * 
	 * @param op
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static String simpleTwoString(String value1, String value2) {
		if(StringUtils.isEmpty(value1) || "null".equals(value1)){
			value1 = "0.0";
		}
		if(StringUtils.isEmpty(value2) || "null".equals(value2)){
			value2 = "0.0";
		}
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();   
		nf.setGroupingUsed(false);
		return nf.format(Double.parseDouble(value1)+Double.parseDouble(value2));
	}
	
	/**
	 * 判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		   for(int i=str.length();--i>=0;){
		      int chr=str.charAt(i);
		      if(chr<48 || chr>57)
		         return false;
		   }
		   return true;
		}
	
	/**
	 * double类型整数不带0
	 * @param d
	 * @return
	 */
	public static String doubleTrans(double d){
		  if(Math.round(d)-d==0){
			  return String.valueOf((long)d);
		  }
		  return String.valueOf(d);
	}
	
	public static void main(String[] args) {
		System.out.println(MathEval.simpleTwoString("1890.0", "null"));
	}
	
	/**
	 *  判断数字大小
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static int compareNumber(String value1, String value2) {
		if(StringUtils.isEmpty(value1) || "null".equals(value1)){
			return 0;
		}
		if(StringUtils.isEmpty(value2) || "null".equals(value2)){
			return 0;
		}
		return Integer.parseInt(value2) - Integer.parseInt(value1);
	}
	
	public static String getBigDecimalValue(BigDecimal bd,String replace){
		if (bd == null) {
			return replace;
		}

		if (isDecimal3(bd.toString())) {// 超过两位小数
			return formatValue(bd.doubleValue());
		} else {
			return bd.toString();
		}
	}

	public static String getDoubleValue(Double db, String replace) {
		if (db == null) {
			return replace;
		}

		if (isDecimal3(db.toString())) {// 超过两位小数
			return formatValue(db.doubleValue());
		} else {
			return db.toString();
		}
	}
	/**
	 * String 保留两位
	 * @author wenhl
	 * @param db
	 * @param replace
	 * @return
	 */
	public static String getStringValue(String db, String replace) {
		if (db == null || "".equals(db)) {
			return replace;
		}

		if (isDecimal3(db)) {// 超过两位小数
			return formatValue(Double.parseDouble(db));
		} else {
			return db;
		}
	}
	//判断是否超过两位小数
	public static  boolean isDecimal3(String str){  
		  return Pattern.compile("\\d+(\\.[\\d]{3,})").matcher(str).matches();  
	}  
	
	public static String formatValue(double value){
		//截取2位小数
		DecimalFormat   df   =new   DecimalFormat("#0.00"); 
		return df.format(value);
	}
	/**
	 * double 转 bigDecimal转两位小数
	 * @author wenhl
	 * @param db
	 * @return
	 */
	public static BigDecimal doubleToBigDecimal(double db){
		BigDecimal temp = new BigDecimal(db);
		return temp.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	/**
	 * String 转 bigDecimal转两位小数
	 * @author wenhl
	 * @param db
	 * @return
	 */
	public static BigDecimal stringToBigDecimal(String db){ 
		if(db == null || "".equals(db))
			return null;
		BigDecimal temp = new BigDecimal(db);
		return temp.setScale(2,BigDecimal.ROUND_HALF_UP);
	}

	/***
	 * BigDecimal 数值减法计算
	 * @param newData
	 * @param oldData
	 * @return
	 */
	 public static double subtract(BigDecimal newData, BigDecimal oldData){
		 if(newData  != null && oldData != null){
			 return newData.subtract(oldData).doubleValue();
		 }else if (oldData == null){
			 return newData.doubleValue();
		 }else if (newData == null){
			 return oldData.doubleValue();
		 }
		return 0;
	 }

	/***
	 * 生成[min,max]之间的随机数
	 * random.nextInt(max)表示生成[0,max]之间的随机数，然后对(max-min+1)取模。
	 * 以生成[10,20]随机数为例，首先生成0-20的随机数，然后对(20-10+1)取模得到[0-10]之间的随机数，然后加上min=10，最后生成的是10-20的随机数
	 * @param min
	 * @param max
	 * @return
	 */
	public static double randomNum(int  min, int max) {
		Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        return num;
    }

	// 是否是数字
	public static boolean isNumeric1(String str) {
		Pattern pattern = Pattern.compile("(-)?([1-9]+[0-9]*)(\\.[\\d]+)?");// 判断小数
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}