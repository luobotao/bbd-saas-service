package com.bbd.saas.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Created by liyanlei on 2016/8/4 10:59.
 */
public class ListTest {
    public static void main(String[] args) {
        List<String> nameList = new ArrayList<>();
        testList(nameList);
        System.out.println("start");
        if(nameList != null){
            for (String name : nameList){
                System.out.println(name);
            }
        }
        System.out.println("end");
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
