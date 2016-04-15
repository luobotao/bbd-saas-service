package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 运单分派状态
 * Created by liyanlei on 2016/4/11.
 */
public enum DispatchStatus {

	NOTDISPATCH(1, "未分派"),
    DISPATCHED(2, "已分派");
    private int status;
    private String message;
    private DispatchStatus(int status, String message) {
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
        DispatchStatus[] dispatchStatus = DispatchStatus.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (DispatchStatus ds : dispatchStatus) {
            if (id == ds.status) {
                sb.append(Htmls.generateSelectedOption(ds.status,ds.message));
            } else {
                sb.append(Htmls.generateOption(ds.status, ds.message));
            }
        }
        return sb.toString();
    }
    public static DispatchStatus status2Obj(int value) {
    	DispatchStatus[] dispatchStatus = DispatchStatus.values();
        for (DispatchStatus ds : dispatchStatus) {
            if (value == ds.status) {
                return ds;
            }
        }
        return null;
    }
}
