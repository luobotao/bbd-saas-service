package com.bbd.saas.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by luobotao on 2016/4/25.
 */
public class StringUtil {
    // 组装签名字符串,供外部使用
    public static String makeOpenSig(Map<Object, Object> sortMap, String token) {
        StringBuilder sb = new StringBuilder();
        Object[] keys = sortMap.keySet().toArray();
        Arrays.sort(keys);
        for (int i = 0; i < keys.length; i++) {
            String mapkey = (String) keys[i];
            sb.append(mapkey).append("=").append(sortMap.get(mapkey)).append("&");
        }
        sb.append(token);//拼接token
        String data = sb.toString();// 参数拼好的字符串
        try {
            MessageDigest messageDigest = java.security.MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < digest.length; i++) {
                String shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            data = hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("加密参数为：" + sb.toString() + "结果：" + data);
        return data;
    }
}
