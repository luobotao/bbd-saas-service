package com.bbd.saas.utils;

public class Htmls {
	private static final String OPTION = "<option value=\"%d\">%s</option>";

	private static final String SELECTED_OPTION = "<option selected value=\"%d\">%s</option>";
	
	private static final String OPTIONString = "<option value=\"%s\">%s</option>";
	
	private static final String SELECTED_OPTIONString = "<option selected value=\"%s\">%s</option>";

	private static final String OPTION_NAME = "<option value=\"%s\" name=\"%s\">%s</option>";
	private static final String SELECTED_OPTION_NAME = "<option selected value=\"%s\" name=\"%s\">%s</option>";

	private static final String MULTIOPTION = "<li><label class=\"f12 linputC\">" +
			"<input type=\"checkbox\" name=\"%s\" value=\"%d\" isAll=\"0\"><b>%s</b></label></li>";
	private static final String SELECTED_MULTIOPTION = "<li><label class=\"f12 linputC\">" +
			"<input type=\"checkbox\" name=\"%s\" value=\"%d\" checked isAll=\"0\"><b>%s</b></label></li>";
	private static final String SELECTED_MULTIOPTION_ALL = "<li><label class=\"f12 linputC\">" +
			"<input type=\"checkbox\" name=\"%s\" value=\"%d\" isAll=\"1\"><b>%s</b></label></li>";

	/**
	 * value为数值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String generateOption(Object key, Object value) {
		return String.format(OPTION, key, value);
	}

	/**
	 * value为数值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String generateSelectedOption(Object key, Object value) {
		return String.format(SELECTED_OPTION, key, value);
	}
	
	
	/**
	 * value为String
	 * @param key
	 * @param value
	 * @return
	 */
	public static String generateOptionString(Object key, Object value) {
		return String.format(OPTIONString, key, value);
	}
	
	/**
	 * value为String
	 * @param key
	 * @param value
	 * @return
	 */
	public static String generateSelectedOptionString(Object key, Object value) {
		return String.format(SELECTED_OPTIONString, key, value);
	}
	
	/**
	 * 
	 * <p>Title: generateOptionName</p> 
	 * <p>Description: 生成select带name、key、value</p> 
	 * @param key
	 * @param name
	 * @param value
	 * @return
	 */
	public static String generateOptionName(Object key,Object name, Object value) {
		return String.format(OPTION_NAME, key, name, value);
	}
	
	/**
	 * 
	 * <p>Title: generateSelectedOptionName</p> 
	 * <p>Description: 生成选中的select带name、key、value</p> 
	 * @param key
	 * @param name
	 * @param value
	 * @return
	 */
	public static String generateSelectedOptionName(Object key, Object name, Object value) {
		return String.format(SELECTED_OPTION_NAME, key, name, value);
	}


	/**
	 * 产生多选框选项
	 * @param name inp
	 * @param value
	 * @param showName
     * @return
     */
	public static String generateMultiOption(Object name, Object value, Object showName) {
		return String.format(MULTIOPTION, name, value, showName);
	}

	/**
	 * 产生多选框选中的选项
	 * @param name inp
	 * @param value
	 * @param showName
	 * @return
	 */
	public static String generateMultiSelectedOption(Object name, Object value, Object showName) {
		return String.format(SELECTED_MULTIOPTION, name, value, showName);
	}
	/**
	 * 产生多选框选中的选项--全选
	 * @param name inp
	 * @param value
	 * @param showName
	 * @return
	 */
	public static String generateMultiSelectedOptionAll(Object name, Object value, Object showName) {
		return String.format(SELECTED_MULTIOPTION_ALL, name, value, showName);
	}
}
