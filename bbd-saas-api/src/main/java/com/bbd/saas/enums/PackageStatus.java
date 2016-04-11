package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 包裹状态--用于数据查询页面
 * Created by liyanlei on 2016/4/11.
 */
public enum PackageStatus {

	NOTARR(0, "未到站"),
	//ARRIVED(1,"已扫描到站"),
	NOTDISPATCH(1, "未分派"),
	DISPATCHED(2, "已分派"),
	RETENTION(3, "滞留"),
    REJECTION(4, "拒收"),
	SIGNED(5, "已签收"),
	TO_OTHER_EXPRESS(6, "已转其他快递"),
	APPLY_RETURN(7, "申请退货"),
	RETURNED(8, "退货完成");
    private int status;
    private String message;
    private PackageStatus(int status, String message) {
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
        PackageStatus[] packageStatus = PackageStatus.values();
        sb.append(Htmls.generateOption(-1, "默认全部"));
        for (PackageStatus ps : packageStatus) {
			if (id == ps.status) {
                sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
            } else {
                sb.append(Htmls.generateOption(ps.status, ps.message));
            }
        }
        return sb.toString();
    }
    public static PackageStatus status2Obj(int value) {
    	PackageStatus[] packageStatus = PackageStatus.values();
        for (PackageStatus ps : packageStatus) {
            if (value == ps.status) {
                return ps;
            }
        }
        return null;
    }
}
