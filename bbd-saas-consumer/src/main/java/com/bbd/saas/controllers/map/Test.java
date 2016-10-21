package com.bbd.saas.controllers.map;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Created by liyanlei on 2016/8/8 17:06.
 */
public class Test {
    public static void main(String[] args) {
        String str = "nice to meet you";
        System.out.println(str.substring(0, str.length()));

        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        list.add("five");
        list.add("six");
        list.add("seven");
        System.out.println(list.subList(0,3).size());
    }
}
