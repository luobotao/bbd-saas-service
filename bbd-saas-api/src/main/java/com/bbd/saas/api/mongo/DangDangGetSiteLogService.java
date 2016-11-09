package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.DangDangGetSiteLog;

import java.util.List;

/**
 * Created by liyanlei on 2016/11/09.
 * 当当运单获取目标站点的日志接口
 */
public interface DangDangGetSiteLogService {

    /**
     * 根据运单号集合查询记录集合
     * @param orderIds 运单号集合
     * @return  记录集合
     * @throws Exception
     */
    public List<DangDangGetSiteLog> findListByOrderIds(String [] orderIds) throws Exception;
}
