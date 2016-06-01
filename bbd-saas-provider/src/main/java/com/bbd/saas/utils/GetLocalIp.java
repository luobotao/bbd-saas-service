package com.bbd.saas.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取本机IP
 * Created by botao on 2016/6/1.
 */
public class GetLocalIp {

    /**
     * 获取本机的IP
     *
     * @return Ip地址
     */
    public static String getLocalHostIP() {
        String ip;
        try {
            /**返回本地主机。*/
            InetAddress addr = InetAddress.getLocalHost();
            /**返回 IP 地址字符串（以文本表现形式）*/
            ip = addr.getHostAddress();
        } catch (Exception ex) {
            ip = "";
        }

        return ip;
    }

    /**
     * 或者主机名：
     *
     * @return
     */
    public static String getLocalHostName() {
        String hostName;
        try {
            /**返回本地主机。*/
            InetAddress addr = InetAddress.getLocalHost();
            /**获取此 IP 地址的主机名。*/
            hostName = addr.getHostName();
        } catch (Exception ex) {
            hostName = "";
        }

        return hostName;
    }

    /**
     * 获得本地所有的IP地址
     *
     * @return
     */
    public static String[] getAllLocalHostIP() {

        String[] ret = null;
        try {
            /**获得主机名*/
            String hostName = getLocalHostName();
            if (hostName.length() > 0) {
                /**在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。*/
                InetAddress[] addrs = InetAddress.getAllByName(hostName);
                if (addrs.length > 0) {
                    ret = new String[addrs.length];
                    for (int i = 0; i < addrs.length; i++) {
                        /**.getHostAddress()   返回 IP 地址字符串（以文本表现形式）。*/
                        ret[i] = addrs[i].getHostAddress();
                    }
                }
            }

        } catch (Exception ex) {
            ret = null;
        }

        return ret;
    }

    public static void main(String[] args) {
        System.out.println("本机IP：" + getLocalHostIP());
        System.out.println("本地主机名字为：" + getLocalHostName());

        String[] localIP = getAllLocalHostIP();
        for (int i = 0; i < localIP.length; i++) {
            System.out.println(localIP[i]);
        }

        InetAddress baidu = null;
        try {
            baidu = InetAddress.getByName("www.bangbangda.cn");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("baidu : " + baidu);
        System.out.println("baidu IP: " + baidu.getHostAddress());
        System.out.println("baidu HostName: " + baidu.getHostName());
    }
}
