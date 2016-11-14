package com.bbd.saas.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Created by liyanlei on 2016/8/4 10:59.
 */
public class ListTest {
    public static void main(String[] args) {
        Thread [] strs = new Thread[10];
        Thread thread=null;
        List<String> nameList = new ArrayList<>();
        testList(nameList);
        System.out.println("start");
        if(nameList != null){
            for (String name : nameList){
                System.out.println(name);
            }
        }
        System.out.println("end");
        /*String mailNumStr="34357567647\n" +
                "34358197489\n" +
                "34357705501\n" +
                "34386720512";*/
        String mailNumStr="BBD186387937\r\n" +
                "34358197489\n" +
                "34357705501\n" +
                "BBD186866422\r\n" +
                "BBD186866457\r\n" +
                "34358197489\n" +
                "34357705501\n" +
                "BBD186866463";
        String [] mailNums = mailNumStr.split("\n|\r\n");
        for(String mailNum : mailNums){
            System.out.println(mailNum);
        }
    }
    private static void testList(List<String> nameList){
        if(nameList == null){
            nameList = new ArrayList<>();
        }
        nameList.add("曹操");
        nameList.add("刘备");
        nameList.add("孙策");
        nameList.add("孙权");
    }
}
