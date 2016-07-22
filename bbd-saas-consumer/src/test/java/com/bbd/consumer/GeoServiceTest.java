package com.bbd.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class GeoServiceTest {


    //junit.framework.TestCase时用
    public void setUp() throws Exception{
        System.out.println("set up");
        // 生成成员变量的实例
    }
    //junit.framework.TestCase时用
    public void tearDown() throws Exception{
        System.out.println("tear down");
    }

    @Test
    public void testSave() throws Exception{

        Assert.isTrue(true);//无用
    }


}