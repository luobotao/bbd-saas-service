package com.bbd.saas.utils;

/**
 * 常用的业务逻辑代码
 * @description: Created by liyanlei on 2016/8/30 16:14.
 */
public class CommonUtil {

    /**
     * 获得详细地址字符串
     * @param province 省
     * @param city 市
     * @param district 区
     * @param address 详细地址
     * @return 详细地址字符串
     */
    public static String getAddress(String province, String city, String district, String address, String jointStr){
        StringBuffer address_SB= new StringBuffer("");
        if(StringUtil.isNotEmpty(province)){
            address_SB.append(province);
            address_SB.append(jointStr);
        }
        if(StringUtil.isNotEmpty(city)){
            address_SB.append(city);
            address_SB.append(jointStr);
        }
        if(StringUtil.isNotEmpty(district)){
            address_SB.append(district);
            address_SB.append(jointStr);
        }
        if(StringUtil.isNotEmpty(address)){
            address_SB.append(jointStr);
        }
        return address_SB.toString();
    }

}
