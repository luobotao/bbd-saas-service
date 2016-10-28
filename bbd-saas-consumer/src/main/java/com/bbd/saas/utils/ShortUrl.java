package com.bbd.saas.utils;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;

/**
 * 调用新浪API生成短链接
 * Created by luobotao on 16/10/28.
 */
public class ShortUrl {
    private static String appkey = "3271760578";
    private static String apiUrl = "http://api.t.sina.com.cn/short_url/shorten.json?";//新浪短网址API地址


    /**
     * 调用新浪API生成短链接
     * @param longUrl
     * @return
     */
    public static String generateShortUrl(String longUrl) {
        String result = "";
        StringBuilder targetUrl = new StringBuilder(apiUrl);
        targetUrl.append("source=");
        targetUrl.append(appkey);
        targetUrl.append("&url_long=");
        targetUrl.append(longUrl);
        try {
            String response = HttpClientUtil.getInstance().sendHttpGet(targetUrl.toString());
            JSONArray jsonArray = (JSONArray) JSON.parse(response);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            result = String.valueOf(jsonObject.get("url_short"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
