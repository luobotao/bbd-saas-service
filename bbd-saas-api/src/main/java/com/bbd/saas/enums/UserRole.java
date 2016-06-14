package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * 用户角色
 * Created by liyanlei on 2016/4/11.
 */
public enum UserRole {
	SITEMASTER(0, "站长"),
    SENDMEM(1, "派件员"),
    COMPANY(2, "公司管理员"),
    MERCHANT(3,"商户");
    private int status;
    private String message;
    private UserRole(int status, String message) {
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
        UserRole[] returnReason = UserRole.values();
        sb.append(Htmls.generateOption(-1, "全部"));
        for (UserRole rr : returnReason) {
            if (id == rr.status) {
                sb.append(Htmls.generateSelectedOption(rr.status,rr.message));
            } else {
                sb.append(Htmls.generateOption(rr.status, rr.message));
            }
        }
        return sb.toString();
    }
    
    public static String Srcs2HTMLForUser(Integer id) {
        StringBuilder sb = new StringBuilder();
        UserRole[] returnReason = UserRole.values();
        sb.append(Htmls.generateOption(-1, "请选则角色"));
        for (UserRole rr : returnReason) {
            if (id == rr.status) {
                sb.append(Htmls.generateSelectedOption(rr.status,rr.message));
            } else {
                sb.append(Htmls.generateOption(rr.status, rr.message));
            }
        }
        return sb.toString();
    }
    
    public static UserRole obj2UserRole(String objStr) {
       
    	for(UserRole userRole: UserRole.values()){
    		if(userRole.toString().equals(objStr)){
    			return userRole;
    		}
    	}
		return null;
    }
    
    public static UserRole status2Obj(int value) {
    	UserRole[] returnReason = UserRole.values();
        for (UserRole rr : returnReason) {
            if (value == rr.status) {
                return rr;
            }
        }
        return null;
    }
}
