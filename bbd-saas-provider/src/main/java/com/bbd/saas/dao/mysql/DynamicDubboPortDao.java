package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.DubboPort;

/**
 * dubbo管理
 *
 * @author luobotao
 */
public interface DynamicDubboPortDao {


    /**
     * 根据IP获取指定的端口
     *
     * @param ip
     * @return
     */
    DubboPort findPortByIp(String ip);
}
