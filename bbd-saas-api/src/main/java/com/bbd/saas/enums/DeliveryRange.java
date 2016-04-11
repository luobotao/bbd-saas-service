package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 配送范围
 * Created by liyanlei on 2016/4/11.
 */
public enum DeliveryRange {

	TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    ELEVEN(11, "11"),
    TWELVE(12, "12"),
    THIRTEEN(13, "13"),
    FOURTEEN(14, "14"),
    FIFTEEN(15, "15"),
    SIXTEEN(16, "16"),
    SEVETEEN(17, "17"),
    EIGHTEEN(18, "18"),
    NINETEEN(19, "19"),
    TWENTY(20, "20");
    private int status;
    private String message;
    private DeliveryRange(int status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public int getStatus() {
        return status;
    }
    public static String Srcs2HTML(Integer id) {
        StringBuilder sb = new StringBuilder();
        DeliveryRange[] deliveryRange = DeliveryRange.values();
        sb.append(Htmls.generateOption(1, "1"));
        for (DeliveryRange dr : deliveryRange) {
			if (id == dr.status) {
                sb.append(Htmls.generateSelectedOption(dr.status,dr.message));
            } else {
                sb.append(Htmls.generateOption(dr.status, dr.message));
            }
        }
        return sb.toString();
    }
    public static DeliveryRange status2Obj(int value) {
    	DeliveryRange[] deliveryRange = DeliveryRange.values();
        for (DeliveryRange dr : deliveryRange) {
			if (value == dr.status) {
				return dr;
			}
        }
        return null;
    }
}
