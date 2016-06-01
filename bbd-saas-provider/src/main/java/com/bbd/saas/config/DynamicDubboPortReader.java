package com.bbd.saas.config;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.bbd.saas.dao.mysql.DynamicDubboPortDao;
import com.bbd.saas.models.DubboPort;
import com.bbd.saas.utils.GetLocalIp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;

/**
 * Created by botao on 2016/5/31.
 */
public class DynamicDubboPortReader {
    private ConfigurableApplicationContext applicationContext = null;

    private static final String PROTOCOL_NAME = "dubbo";

    private static int RPOTOCOL_PORT = 20881;

    @Resource
    private DynamicDubboPortDao dynamicDubboPortDao;//获取端口服务

    public DynamicDubboPortDao getDynamicDubboPortDao() {
        return dynamicDubboPortDao;
    }

    public void setDynamicDubboPortDao(DynamicDubboPortDao dynamicDubboPortDao) {
        this.dynamicDubboPortDao = dynamicDubboPortDao;
    }

    /*初始化方法*/
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:META-INF/spring/provider-protocol.xml"});
        setApplicationContext(applicationContext);
        DubboPort dubboPort = dynamicDubboPortDao.findPortByIp(GetLocalIp.getLocalHostIP());
        if(dubboPort!=null){
            RPOTOCOL_PORT = dubboPort.getPort();
        }
        updateProtocolMessage(PROTOCOL_NAME, RPOTOCOL_PORT);
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public void updateProtocolMessage(String protocolConfig, int port) {
        //判断初始化
        if (!applicationContext.containsBean(protocolConfig)) {
            System.out.println("没有【" + protocolConfig + "】协议");
        }
        //获取协议
        ProtocolConfig protocolConfigSource = (ProtocolConfig) applicationContext.getBean(protocolConfig);
        //修改协议暴露端口信息
        protocolConfigSource.setPort(port);
    }
}
