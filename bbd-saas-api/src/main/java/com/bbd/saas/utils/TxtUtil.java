package com.bbd.saas.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: Created by liyanlei on 2016/9/6 16:40.
 */
public class TxtUtil {
    // 写文件
    public void writerTxt() {
        BufferedWriter fw = null;
        try {
            File file = new File("D://text.txt");
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
            fw.append("我写入的内容");
            fw.newLine();
            fw.append("我又写入的内容");
            fw.flush(); // 全部写入缓存中的内容
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读文件
     * @param filePath 文件路径
     */
    public List<String> readTxt(String filePath) {
        BufferedReader reader = null;
        List<String> contents = new ArrayList<String>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "gbk")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
            String line = null;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return contents;
    }

    public static void main(String[] args) {
        TxtUtil txtUtil = new TxtUtil();
        String filePath = "E:\\work\\testData\\city.txt";
        txtUtil.readTxt(filePath);
    }
}
