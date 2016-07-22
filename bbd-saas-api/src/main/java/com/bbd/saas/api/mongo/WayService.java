package com.bbd.saas.api.mongo;

/**
 * Created by liyanlei on 2016/7/22.
 * 站点接口
 */
public interface WayService {

    /**
     * 查询经过指定站点的路线的条数
     * @param siteId 站点Id
     * @return 路线的条数
     */
    long findWayBySiteId(String siteId);

}
