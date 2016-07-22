package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.WayService;
import com.bbd.saas.dao.mongo.WayDao;

/**
 * Created by liyanlei on 2016/7/22.
 * 管理员接口
 */
public class WayServiceImpl implements WayService {
    private WayDao wayDao;

    public WayDao getWayDao() {
        return wayDao;
    }

    public void setWayDao(WayDao wayDao) {
        this.wayDao = wayDao;
    }

    @Override
    public long findWayBySiteId(String siteId) {
        return wayDao.selectWayBySiteId(siteId);
    }
}
