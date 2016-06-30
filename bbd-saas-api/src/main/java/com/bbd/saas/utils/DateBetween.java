package com.bbd.saas.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateBetween {
	private final Pattern pattern = Pattern
			.compile("(\\d{4}/(?:0?[1-9]|1[0-2])/(?:0?[0-9]|[1-2][0-9]|3[0,1]))\\s-\\s(\\d{4}/(?:0?[1-9]|1[0-2])/(?:0?[0-9]|[1-2][0-9]|3[0,1]))");

	private Date start = new Date();
	private Date end = new Date();
	public DateBetween() {

	}

	public DateBetween(String between) {
		Matcher matcher = pattern.matcher(between);
		if (matcher.matches()) {
			start = Dates.parseDate(matcher.group(1));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			end = Dates.parseDate(matcher.group(2));
			Calendar cal = Calendar.getInstance();
			cal.setTime(end);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 0);
			end = cal.getTime();
		}
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Dates.formatSimpleDate(start)).append(" - ")
				.append(Dates.formatSimpleDate(end));
		return sb.toString();
	}
	public String toSimpleString() {
		StringBuilder sb = new StringBuilder();
		sb.append(Dates.formatDate(start)).append("-")
		.append(Dates.formatDate(end));
		return sb.toString();
	}
	public  List<String> splitDate(String between){
		List<String> list = new ArrayList<String>();
		Matcher matcher = pattern.matcher(between);
		if (matcher.matches()) {
			list.add(matcher.group(1));
			list.add(matcher.group(2));
		}
		return list;
	}
}
