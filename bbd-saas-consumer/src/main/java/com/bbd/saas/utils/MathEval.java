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
	 * 计算带小括号的公式
	 * 
	 * @param line
	 * @return
	 */
	public static double eval(String line) {
		while (line.indexOf(BRACKET_LEFT) != -1) {
			Pattern pattern = Pattern.compile("\\(([^\\(\\)]*?)\\)");
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				double result = simpleEval(matcher.group(1));
				line = line.replace(matcher.group(), result + "");
			}
		}
		return simpleEval(line);
	}

	/**
	 * 计算不带括号的公式
	 * 
	 * @param line
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static double simpleEval(String line) {
		Stack<Double> valueStack = new Stack<Double>();// 保存值的堆栈
		Stack<Character> markStack = new Stack<Character>();// 保存符号的堆栈

		char ch[] = line.toCharArray();

		// 计算乘除操作
		String tmpValue = "";
		boolean isOper = false;
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == PLUS || ch[i] == MINUS || ch[i] == MULTI
					|| ch[i] == DEVIDE) {
				double dv = Double.valueOf(tmpValue).doubleValue();
				if (isOper) {
					double dv1 = valueStack.pop();
					char op = markStack.pop();
					double result = simpleTwoEval(op, dv1, dv);
					dv = result;
				}
				valueStack.push(dv);
				markStack.push(ch[i]);
				tmpValue = "";
				isOper = false;
				if (ch[i] == MULTI || ch[i] == DEVIDE)
					isOper = true;
			} else {
				tmpValue += ch[i] + "";

				if (i == ch.length - 1) {
					double dv = Double.valueOf(tmpValue).doubleValue();
					if (isOper) {
						double dv1 = valueStack.pop();
						char op = markStack.pop();
						double result = simpleTwoEval(op, dv1, dv);
						dv = result;
					}
					valueStack.push(dv);
				}
			}
		}
		// for(int i=0; i< valueStack.size(); i++){
		// System.out.println(valueStack.get(i));
		// }
		// for(int i=0; i< markStack.size(); i++){
		// System.out.println(markStack.get(i));
		// }

		// 计算加减操作
		valueStack = (Stack<Double>) reverseStack(valueStack);
		markStack = (Stack<Character>) reverseStack(markStack);
		while (valueStack.size() > 1) {
			double v1 = valueStack.pop();
			double v2 = valueStack.pop();
			char op = markStack.pop();
			double result = simpleTwoEval(op, v1, v2);
			valueStack.push(result);
		}
		return valueStack.get(0);
	}

	/**
	 * 把整个堆栈翻转
	 * 
	 * @param stack
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Stack<?> reverseStack(Stack<?> stack) {
		Stack reverse = new Stack();
		int stackSize = stack.size();
		for (int i = 0; i < stackSize; i++) {
			reverse.push(stack.pop());
		}
		return reverse;
	}

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
	public static Double simpleTwo(Double value1, Double value2) {
		if(value1 == null){
			value1 = 0.0;
		}
		if(value2 == null){
			value2 = 0.0;
		}
		
		return value1+value2;
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
	/**
	 *  判断月份大小
	 * @param value1
	 * @param value2  search.getEndMonth() - search.getStartMonth()
	 * @return  MathEval.compareMonth(search.getStartMonth(),search.getEndMonth()
	 */
	public static int compareMonth(int value1, int value2){
		if(value1 == 0){
			return 0;
		}
		if(value2 == 0){
			return 0;
		}
		return value2-value1;
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