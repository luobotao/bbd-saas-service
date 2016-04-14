package com.bbd.saas.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;



public class StrTool {

	/**
	 * 字符串为空时替换称指定文字
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String initStr(String oldString,String newString){
		if (oldString == null || "".equals(oldString) || "null".equals(oldString) || "undefined".equals(oldString))
			return newString;
		else
			return oldString;
	}
	
	/**
	 * 取得指定字符串中{}内容,
	 * @param str
	 * @return 多个{}中内容的集合
	 */
	public static final List<String> split(String str) {
		List<String> result = new ArrayList<String>();
		String[] st = str.split("\\{");
		for (int i = 1; i < st.length; i++) {
			int index = st[i].indexOf("}");
			if(index > -1){
				result.add(st[i].substring(0, st[i].indexOf("}")));
			}
		}
		return result;
	}

	/**
	 * 取得公式中的参数
	 * @param formula
	 * @return
	 */
	public static final List<String> getFormulaPara(String formula) {
		List<String> ids = new ArrayList<String>();
		String[] st = formula.split("【");
		for (int i = 1; i < st.length; i++) {
			ids.add(st[i].substring(0, st[i].indexOf("】")));
		}
		return ids;
	}
	
	/**
	 * 判断是否周末
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static boolean isWeekend(String date) throws ParseException {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Date bdate = format1.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(bdate);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}
	
	/**
	 * 字符串截取
	 * @param title
	 * @param length
	 * @return
	 */
	public static String subStringByBytes(String title, int length) {
		String fullTitle = title;

		if(fullTitle == null){
			return "";
		}

		if (fullTitle.getBytes().length <= length) {
			return title;
		}
		StringBuilder sb = new StringBuilder();
		int countLength = 0;
		for (int i = 0; i < fullTitle.length(); i++) {
			countLength = countLength
					+ fullTitle.substring(i, i + 1).getBytes().length;
			if (countLength > length) {
				break;
			}
			sb.append(fullTitle.substring(i, i + 1));
		}
		return sb.toString();
	}
	/**
	 * 指定起始的字符串截取
	 * @param title
	 * @param length
	 * @return
	 */
	public static String subStringByBytesWithStart(String title, int start,int end) {
		String fullTitle = title;
		if(fullTitle == null || fullTitle.getBytes().length < start){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		if(fullTitle.length()<end){
			end = fullTitle.length();
		}
		for (int i = start; i < end; i++) {
			sb.append(fullTitle.substring(i, i + 1));
		}
		return sb.toString();
	}
	/**
	 * 将[aa,bb,cc]样式的字符创装换成list
	 * @param itemids
	 * @return
	 */
	public static List<String> generateList(String itemids){
		List<String> ret = new ArrayList<String>();
		if(!StringUtils.hasText(itemids)){
			return ret;
		}else{
			String subStr = itemids.substring(1, itemids.length()-1);
			String[] ids = subStr.split(",");
			for (String str : ids) {
				ret.add(str.trim());
			}
		}
		return ret;
	}
	
	/**
	 * 去掉字符串中的空格
	 * @param str
	 * @return
	 */
	public static final String trimString(String str){
		if(str==null||"".equals(str)){
			str = "";
		}
		String[] split = new String[str.length()];
		String ret = "";
		if(str.length()>0){
			for(int i=0; i<str.length(); i++) {
	            split[i] = str.substring(i, i+1);
	        }
	        for(int i=0; i<str.length(); i++) {
	            if(split[i].equals(" ")){
	                continue;
	            }else {
	            	ret = ret + split[i];  
	            }
	        }
		}
        return ret;
	}
	/** 
	 * 去除字符串中头部和尾部所包含的空格（包括:空格(全角，半角)、制表符、换页符等） 
	 * @param s 
	 * @return 
	 */ 
	public static String trimStarAndEnd(String s){  
	    String result = "";  
	    if(null!=s && !"".equals(s)){  
	        result = s.replaceAll("^[　*| *| *|//s*]*", "").replaceAll("[　*| *| *|//s*]*$", "");  
	    }  
	    return result;  
	} 
	
	
	/**
	 * 数组称Like模糊查询的检索条件字符串
	 * @param arrays
	 * @return
	 */
	public static String arrayToSql(String[] arrays,String column){
		
		//判断空
		if(arrays == null){
			return "";
		}

		//产品模糊查询
		StringBuilder sql = new StringBuilder();
		sql.append("AND (");
		for(int i =0;i<arrays.length;i++){
			if(i != 0){
				sql.append(" OR ");
			}
			sql.append(column+" LIKE '"+arrays[i]+"%'");
		}
		sql.append(")");
		return sql.toString();
	}
	
	
	/**
	 * 将小数转换成两位小数，整数的不变
	 * @param num
	 * @return
	 */
	public static String toDoubleDecimal(Double num){
		String pString = "";
		if(num!=null){
			String number = String.valueOf(num);
			String[] str = number.split("\\.");
			if(str.length>1){
				String sString = str[0];
				String eString = str[1];
				if(eString.length()==1){
					if("0".equals(eString)){
						pString = sString;
					}else{
						pString = sString + "." + eString + "0";
					}
				}else if(eString.length()>=2){
					String edString = eString.substring(0,2);
					pString = sString + "." + edString;
				}
			}else{
				pString = number;
			}
		}
		return pString;
	}
	 public static String Html2Text(String inputString) {      
	        String htmlStr = inputString; // 含html标签的字符串      
	        String textStr = "";      
	        java.util.regex.Pattern p_script;      
	        java.util.regex.Matcher m_script;      
	        java.util.regex.Pattern p_style;      
	        java.util.regex.Matcher m_style;      
	        java.util.regex.Pattern p_html;      
	        java.util.regex.Matcher m_html;      
	    
	        java.util.regex.Pattern p_html1;      
	        java.util.regex.Matcher m_html1;      
	    
	       try {      
	            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>      
	            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>      
	            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式      
	            String regEx_html1 = "<[^>]+";      
	            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);      
	            m_script = p_script.matcher(htmlStr);      
	            htmlStr = m_script.replaceAll(""); // 过滤script标签      
	    
	            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);      
	            m_style = p_style.matcher(htmlStr);      
	            htmlStr = m_style.replaceAll(""); // 过滤style标签      
	    
	            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);      
	            m_html = p_html.matcher(htmlStr);      
	            htmlStr = m_html.replaceAll(""); // 过滤html标签      
	    
	            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);      
	            m_html1 = p_html1.matcher(htmlStr);      
	            htmlStr = m_html1.replaceAll(""); // 过滤html标签      
	    
	            textStr = htmlStr;      
	    
	        } catch (Exception e) {      
	            System.err.println("Html2Text: " + e.getMessage());      
	        }      
	    
	       return textStr;// 返回文本字符串      
	  }     
	 
	 /**获取二维数组的名称集合
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static List<String> getListFromTwoArray(String[][] statusArray){
		List<String> list = new ArrayList<String>();
		for(String[] status :statusArray){
			list.add(status[1]);
		}
		return list;
	}
	

	/**
	 * 当前月份加1
	 * @param date
	 * @return
	 */
	public static Date monthPlus(Date date){
		//结束日期为下个月1日0点
		Calendar calendar=Calendar.getInstance();   
		calendar.setTime(date);
		calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+1);//月份+1
		return calendar.getTime();
	}
	
	/**
	 * 百分率转换
	 * @param 数字
	 * @return 转换后数字
	 */
	public static BigDecimal conversionPercent(String str){
		//判断字符串中是否有%
		//没有百分号乘以100
		//有百分号直接返回
		if(!StringUtils.hasText(str)){
			return null;
		}
		String[] numArray = str.split("%");
		BigDecimal bd1 = new BigDecimal(numArray[0]);
		if(!str.contains("%")){
			BigDecimal bd2 = new BigDecimal(100);
			//去除小数点最后的00
			return new BigDecimal(MathEval.doubleTrans(bd1.multiply(bd2).doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
		}else{
			return bd1;
		}
	}
	
	public static void main(String[] arg){
		System.out.println(string2Scale(2, "3.1580000"));
		double d = Double.parseDouble("3.1580000") - Double.parseDouble("5.12670000");
		System.out.println(d);
		
		System.out.println(calculateRange("3.1580000", "5.67840000"));
		
		System.out.println(calculateDifference("3.1580000", "5.67840000"));
		
		List<String> data = calculateZDAndRange("3.1580000", "5.67840000");
		
		System.out.println("0==" + data.get(0) +" 1==" + data.get(1));
		//System.out.print(conversionPercent("3.15"));
	}
	
	/**
	 * 校验当前字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		// 负数符号
		String replaceStr = str.replace("-", "").trim();
		// %
		replaceStr = replaceStr.replace("%", "").trim();
		String[] tempStr = replaceStr.replace(".", "_").split("_");
		// 有多个小数点符号
		if (tempStr.length > 2) {
			return false;
		}
		// 一个小数点（正常）
		if (tempStr.length == 2) {
			String num01 = tempStr[0];
			String num02 = tempStr[1];
			// 1. 暂定不符合规范  1.0则可以
			if (num02.length() == 0){
				return false;
			}
			String num03 = num01.concat(num02);
			if (num03.length() > 0){
				return isNumeric(num03);
			}
		}
		// 没有小数点 （正常）
		if(tempStr.length == 1) {
			// 判断是否为数字
			return isNumeric(replaceStr);
		}
		return false;
	}
	
	/**
	 * 判断当前字符串是否为整数，不是通用方法，只针对100.0(示例)格式的数字判断
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isStrInteger(String str) {
		if (str.lastIndexOf(".") != -1) {
			if (str.length() == str.lastIndexOf(".")+2) {
				String tempNum = str.substring(str.lastIndexOf(".")+1, str.lastIndexOf(".")+2);
				if ("0".equals(tempNum)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for(int i = str.length();--i>=0;){
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 校验当前年份是否符合系统规范
	 * 
	 * @param strYear
	 * @return
	 */
	public static boolean isValidateYear(String strYear){
		if (strYear == null || "".equals(strYear)) {
			return false;
		}
		String tempYear = strYear.trim();
		if (tempYear.length() == 4) {
			if (isNumeric(tempYear)) {
				return true;
			}
		}
		if (tempYear.indexOf("年") != -1) {
			if (tempYear.length() == 5) {
				if (isNumeric(tempYear.substring(0, 4))) {
					return true;
				}
			}
		}		
		return false;
	}
	
	/**
	 * 校验当前季节是否符合系统规范
	 * 
	 * @param strYear
	 * @return
	 */
	public static boolean isValidateSeason(String strSeanson){
		if (strSeanson == null || "".equals(strSeanson)) {
			return false;
		}
		String tempYear = strSeanson.trim();
		if (tempYear.length()-1 > tempYear.lastIndexOf("度")) {
			return false;
		}
		if (tempYear.substring(0, tempYear.indexOf("年")).trim().length() > 4) {
			return false;
		}
		
		// 四个季节
		String compareSeanson = "|1|2|3|4|";
		
		if (tempYear.indexOf("年") != -1 && tempYear.indexOf("第") != -1 && tempYear.indexOf("季") != -1) {
			if (isNumeric(tempYear.substring(0, 4))) {
				int start = tempYear.lastIndexOf("第")+1;
				int end = tempYear.lastIndexOf("季");
				if (isNumeric(tempYear.substring(start, end))) {
					Integer tempInt = new Integer(tempYear.substring(start, end));
					if (compareSeanson.contains("|"+tempInt+"|")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 校验当前月份是否符合系统规范
	 * 
	 * @param strMonth
	 * @return
	 */
	public static boolean isValidateMonth(String strMonth){
		if (strMonth == null || "".equals(strMonth)) {
			return false;
		}
		String tempMonth = strMonth.trim();
		if (tempMonth.length()-1 > tempMonth.lastIndexOf("月")) {
			return false;
		}
		if (tempMonth.substring(0, tempMonth.indexOf("年")).trim().length() > 4) {
			return false;
		}
		// 月份
		String compareMonth = "|1|2|3|4|5|6|7|8|9|10|11|12|";
		
		if (tempMonth.indexOf("年") != -1 && tempMonth.indexOf("月") != -1) {
			if (isNumeric(tempMonth.substring(0, 4))) {
				int start = tempMonth.lastIndexOf("年")+1;
				int end = tempMonth.lastIndexOf("月");
				if (isNumeric(tempMonth.substring(start, end))) {
					Integer tempInt = new Integer(tempMonth.substring(start, end));
					if (compareMonth.contains("|"+tempInt+"|")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * 日期格式：
	 * yyyy年mm月dd日
	 * yyyy/mm/dd
	 * yyyy-mm-dd
	 * 
	 * 转换成：
	 * yyyy-mm-dd
	 * 
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String stringDateToYMD(String dateStr) throws Exception {
		try {
			if (initStr(dateStr, null) == null) {
				return null;
			}
			String yearT = dateStr.substring(0, 4);
			String monthT = "";
			String dateT = "";
			if (dateStr.indexOf("年") != -1 && dateStr.indexOf("月") != -1 && dateStr.indexOf("日") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("年")+1, dateStr.indexOf("月")).trim();
				dateT = dateStr.substring(dateStr.indexOf("月")+1, dateStr.indexOf("日")).trim();
			}
			if (dateStr.indexOf("/") != -1 && dateStr.lastIndexOf("/") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("/")+1, dateStr.lastIndexOf("/")).trim();
				dateT = dateStr.substring(dateStr.lastIndexOf("/")+1, dateStr.length()).trim();
			}
			if (dateStr.indexOf("-") != -1 && dateStr.lastIndexOf("-") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("-")+1, dateStr.lastIndexOf("-")).trim();
				dateT = dateStr.substring(dateStr.lastIndexOf("-")+1, dateStr.length()).trim();
			}
			String resultDate = yearT.concat("-").concat(monthT).concat("-").concat(dateT);
			
			return resultDate;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	public static String stringDateToYM(String dateStr) throws Exception {
		try {
			if (initStr(dateStr, null) == null) {
				return null;
			}
			String yearT = dateStr.substring(0, 4);
			String monthT = "";
			if (dateStr.indexOf("年") != -1 && dateStr.indexOf("月") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("年")+1, dateStr.indexOf("月")).trim();
			}
			if (dateStr.indexOf("/") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("/")+1, dateStr.length()).trim();
			}
			if (dateStr.indexOf("-") != -1) {
				monthT = dateStr.substring(dateStr.indexOf("-")+1, dateStr.length()).trim();
			}
			String resultDate = yearT.concat("-").concat(monthT);
			
			return resultDate;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 对时间的解析
	 * @param time
	 * @return
	 */
	public static boolean resolveTime(String time){
		String regex = "[0-9]{4}[年,/,-]{0,1}[0,1]{0,1}[0-9]{0,1}[月,/,-]{0,1}[0,1,2,3]{0,1}[0-9]{0,1}[日]{0,1}";
		if(time.matches(regex)){
			return true;
		}
		return false;
	}
	
	/**
	 * 使用SimpleDateFormat类的parse方法尝试解析字符串，若解析成功则说明字符串是合法的日期字符串，否则则说明输入的字符串不是合法的日期。
	 * @param inDate 需要判断的字符串
	 * @return boolean true:合法  false：非法
	 * */
	public static boolean isValidDate(String inDate) {
		return isValidDate(inDate, "yyyy-MM-dd");
	}
	/**
	 * 使用SimpleDateFormat类的parse方法尝试解析字符串，若解析成功则说明字符串是合法的日期字符串，否则则说明输入的字符串不是合法的日期。
	 * @param inDate 需要判断的字符串
	 * @return boolean true:合法  false：非法
	 * */
	public static boolean isValidDate(String inDate,String dateStyle) {
		if (inDate == null)
			return false;
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateStyle);
		if (inDate.trim().length() != dateFormat.toPattern().length())
			return false;
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
	
	/**
	 *  2011年12月上旬;
		2009年上半年;
		2007年1月初;
		对用户传过来的这三种值做处理。返回xxxx/xx/xx
	 * @param date
	 * @return
	 */
	public static String splitDate(String date){
//		String a = "2011年12月上旬";
//		String b = "2009年上半年";
//		String c = "2007年1月初";
		// 把年先取出来
		String year = "";
		String Month = "";
		String day = "";
		year = date.substring(0, 4);
		// 取月
		String mayDay = "";
		mayDay = date.substring(5, date.length());
		if (mayDay.indexOf("月") > 0) {
			Month = mayDay.substring(0, mayDay.indexOf("月"));
			day = mayDay.substring(mayDay.indexOf("月")+1, mayDay.length());
			if(day.length()==1){
				day = "月"+day;
			}
		}else{
			Month="无";
			day=mayDay;
		}
		return year+"/"+Month+"/"+day;
	}
	//截取2位小数
	/***
	 * 计算的结果 都保留两位小数 By tanfy 2015-11-24
	 * @param double1
	 * @param replace
	 * @return
	 */
	public static String get2NumByDoubleValue(Double double1,String replace){
		if(double1==null){
			return  replace;
		}
		String str=double1.toString();
		if(str.indexOf("E")!=-1){//科学计数法
			BigDecimal db = new BigDecimal(double1);
			str = db.toPlainString();
		}
		return formatValue(double1.doubleValue());
	}
	/**
	 * 截取2位小数
	 * @author wenhl
	 * @param string1
	 * @param replace
	 * @return
	 */
	public static String get2NumByStringValue(String string1,String replace){
		if(string1==null || "".equals(string1)){
			return  replace;
		}
		if(string1.indexOf("E")!=-1){//科学计数法
			BigDecimal db = new BigDecimal(string1);
			string1 = db.toPlainString();
		}
		if(isDecimal(string1)){//超过两位小数
			return formatValue(Double.parseDouble(string1));
		}else{
			return string1;
		}
	}
	// 截取2位小数
	public static String get2NumByPersentage(String value, String replace) {
		if (value == null) {
			return replace;
		}
		if (isDecimal(value)) {// 超过两位小数
			return value.substring(0, value.indexOf(".") + 3) + "%";
		} else {
			return value;
		}

	}

	//判断是否超过两位小数
	public static boolean isDecimal(String str){
		return Pattern.compile("-?\\d+(\\.[\\d]{3,})%?").matcher(str).matches();
	}  
	
	/**
	 * @author liyanlei
	 * @date 2016年3月18日上午9:20:36
	 * Description: 四舍五入截取两位小数
	 * @param value
	 * @return
	 */
	public static String formatValue(double value){
		//截取2位小数
		BigDecimal db = new BigDecimal(value);
		return db.setScale(2, BigDecimal.ROUND_HALF_UP).toString(); 
	}

	// 2015-8-3-->2015-08-03
	//添加0
	public static String addZero(String s) {
		s = s.replaceAll("([-|/])([1-9])([-|/])", "$10$2$3");// 替换月
		return s.replaceAll("([-|/])([1-9])$", "$10$2");// 替换日
	}
	public String removeZero(String date) {
		if (date == null) {
			return null;
		}
		return date.replaceAll("0(\\d)", "$1");
	}
	public static String removeZero2(String date) {
		if (date == null) {
			return null;
		}
		return date.replaceAll("([-|/])0(\\d)", "$1$2");
	}
	/**
	 * 传入字符串，判断是否是小数两位以上(包括两位)，
	 * 如果是则四舍五入保留两位
	 * @param scale 要保留的小数位数
	 * @param str 做四舍五入的字符串
	 * @return
	 */
	public static String string2Scale(int scale,String str){
		//如果是小数且有小数两位以上，保留两位小数
		if(str !=null && str.indexOf(".") != -1){
			String value = str.substring(str.indexOf(".")+1, str.length());
			if(value.length() > 1){
				BigDecimal db = new BigDecimal(str);
				str = db.setScale(scale, BigDecimal.ROUND_HALF_UP).toString(); 
			}
		}
		return str;
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年3月11日上午10:39:39
	 * Description: 计算差值，保留两位小数（newData和oldData都不为空的情况下，才相减计算，否则返回0.00） newData - oldData
	 * @param newData
	 * @param oldData
	 * @param defaultStr
	 * @return
	 */
	public static String calculateDifference(String newData, String oldData) {		
		if (newData != null && !"".equals(newData) && oldData != null && !"".equals(oldData)) {
			double diff = Double.parseDouble(newData) - Double.parseDouble(oldData);
			return StrTool.string2Scale(2, diff+"");
		} 
		return "0.00";
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年3月11日上午10:39:39
	 * Description: 计算差值，保留两位小数（newData和oldData都不为空的情况下，才相减计算，否则返回0.00） newData - oldData
	 * @param newData
	 * @param oldData
	 * @param defaultStr
	 * @return
	 */
	public static String calculateRange(String newData, String oldData) {		
		if (newData != null && !"".equals(newData) && oldData != null && !"".equals(oldData)) {
			double diff = Double.parseDouble(newData) - Double.parseDouble(oldData);
			//幅度
			double range = diff/Double.parseDouble(oldData)*100;
			return StrTool.string2Scale(2, range+"") + "%";
		} 
		return "0.00";
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年3月11日上午10:39:39
	 * Description: 计算涨跌和幅度，保留两位小数（newData和oldData都不为空的情况下，才相减计算，否则返回0.00） 
	 * data0=newData - oldData; data1=(newData - oldData)/oldData
	 * @param newData
	 * @param oldData
	 * @param defaultStr
	 * @return
	 */
	public static List<String> calculateZDAndRange(String newData, String oldData) {	
		List<String> data = new ArrayList<String>();
		if (newData != null && !"".equals(newData) && oldData != null && !"".equals(oldData)) {
			//涨跌
			double diff = Double.parseDouble(newData) - Double.parseDouble(oldData);
			data.add(StrTool.string2Scale(2, diff+""));
			//幅度
			double range = diff/Double.parseDouble(oldData)*100;
			data.add(StrTool.string2Scale(2, range+"") + "%");
			return data;
		} 
		data.add("0.00");	//涨跌
		data.add("0.00");	//幅度
		return data;
	}
	
	/**
	 * @author liyanlei
	 * @date 2016年3月16日下午4:33:31
	 * Description: 计算均值,保留2位小数
	 * @param priceList
	 * @return
	 */
	public static String calculateAvgValue(List<String> dataList) {
		if(dataList == null || dataList.size() == 0){
			return "/";
		}
		double count=0d;
		for (String data : dataList) {
			count += Double.parseDouble(data); // 价格
		}
		double avg = count/dataList.size();
		return StrTool.string2Scale(2, avg+"");
	}
		
}
