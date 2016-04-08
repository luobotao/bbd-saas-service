package com.bbd.saas.enums;

import com.bbd.saas.utils.Htmls;

/**
 * Created by luobotao on 2016/4/7.
 */
public class OrderEnum {


    
    /**
     * Description: 运单分派状态
     * @author liyanlei
     * 2016年4月8日上午11:01:10
     */
    public static enum DispatchStatus {
        NOTDISPATCH(0, "未分派"),
        DISPATCHED(1, "已分派");
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
    
    /**
     * Description: 包裹异常状态
     * @author liyanlei
     * 2016年4月8日上午11:17:50
     */
    public static enum 	AbnormalStatus {
        RETENTION(0, "滞留"),
        REJECTION(1, "拒收");
        private int status;
        private String message;
        private AbnormalStatus(int status, String message) {
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
            AbnormalStatus[] abnormalStatus = AbnormalStatus.values();
            sb.append(Htmls.generateOption(-1, "全部"));
            for (AbnormalStatus abs : abnormalStatus) {
                if (id == abs.status) {
                    sb.append(Htmls.generateSelectedOption(abs.status,abs.message));
                } else {
                    sb.append(Htmls.generateOption(abs.status, abs.message));
                }
            }
            return sb.toString();
        }
        public static AbnormalStatus status2Obj(int value) {
        	AbnormalStatus[] abnormalStatus = AbnormalStatus.values();
            for (AbnormalStatus abs : abnormalStatus) {
                if (value == abs.status) {
                    return abs;
                }
            }
            return null;
        }
    }
    
    /**
     * Description: 退货原因
     * @author liyanlei
     * 2016年4月8日下午12:01:53
     */
    public static enum 	ReturnReason {
        GOOD_DAMAGE(0, "货物破损"),
        TIMEOUT_DILIVERY(1, "超时配送"),
        CLIENT_RETURN(2, "客户端要求退换"),
        OTHER(3, "其他");
        private int status;
        private String message;
        private ReturnReason(int status, String message) {
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
            ReturnReason[] returnReason = ReturnReason.values();
            sb.append(Htmls.generateOption(-1, "全部"));
            for (ReturnReason rr : returnReason) {
                if (id == rr.status) {
                    sb.append(Htmls.generateSelectedOption(rr.status,rr.message));
                } else {
                    sb.append(Htmls.generateOption(rr.status, rr.message));
                }
            }
            return sb.toString();
        }
        public static ReturnReason status2Obj(int value) {
        	ReturnReason[] returnReason = ReturnReason.values();
            for (ReturnReason rr : returnReason) {
                if (value == rr.status) {
                    return rr;
                }
            }
            return null;
        }
    }
    /**
     * Description: 包裹状态--用于数据查询页面
     * @author liyanlei
     * 2016年4月8日上午11:21:56
     */
    public static enum PackageStatus {
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
    /**
     * Description: 配送范围
     * @author liyanlei
     * 2016年4月8日上午11:36:31
     */
    public static enum DeliveryRange {
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
    public static enum UserStatus {
        VALID(1,"有效"),
        INVALID(0,"无效");
        private int status;
        private String message;
        private UserStatus(int status, String message) {
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
            UserStatus[] packageStatus = UserStatus.values();
            sb.append(Htmls.generateOption(-1, "全部"));
            for (UserStatus ps : packageStatus) {
                if (id == ps.status) {
                    sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
                } else {
                    sb.append(Htmls.generateOption(ps.status, ps.message));
                }
            }
            return sb.toString();
        }
        public static UserStatus status2Obj(int value) {
        	UserStatus[] packageStatus = UserStatus.values();
            for (UserStatus ps : packageStatus) {
                if (value == ps.status) {
                    return ps;
                }
            }
            return null;
        }
    }
    
    public static enum RoleStatus {
    	VALID(1,"有效"),
        INVALID(0,"无效");
        private int status;
        private String message;
        private RoleStatus(int status, String message) {
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
            RoleStatus[] packageStatus = RoleStatus.values();
            sb.append(Htmls.generateOption(-1, "全部"));
            for (RoleStatus ps : packageStatus) {
                if (id == ps.status) {
                    sb.append(Htmls.generateSelectedOption(ps.status,ps.message));
                } else {
                    sb.append(Htmls.generateOption(ps.status, ps.message));
                }
            }
            return sb.toString();
        }
        public static RoleStatus status2Obj(int value) {
        	RoleStatus[] packageStatus = RoleStatus.values();
            for (RoleStatus ps : packageStatus) {
                if (value == ps.status) {
                    return ps;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
    	String r = PackageStatus.Srcs2HTML(0);
    	System.out.println("r==="+r);
	}
}
