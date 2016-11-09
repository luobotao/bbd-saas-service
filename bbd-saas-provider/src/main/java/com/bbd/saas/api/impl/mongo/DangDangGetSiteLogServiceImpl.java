package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.DangDangGetSiteLogService;
import com.bbd.saas.dao.mongo.DangDangGetSiteLogDao;
import com.bbd.saas.mongoModels.DangDangGetSiteLog;

import java.util.List;

/**
 * Created by liyanlei on 2016/7/22.
 * 管理员接口
 */
public class DangDangGetSiteLogServiceImpl implements DangDangGetSiteLogService {
    private DangDangGetSiteLogDao dangDangGetSiteLogDao;

    public DangDangGetSiteLogDao getDangDangGetSiteLogDao() {
        return dangDangGetSiteLogDao;
    }

    public void setDangDangGetSiteLogDao(DangDangGetSiteLogDao dangDangGetSiteLogDao) {
        this.dangDangGetSiteLogDao = dangDangGetSiteLogDao;
    }

    @Override
    public List<DangDangGetSiteLog> findListByOrderIds(String[] orderIds) throws Exception {
        return this.dangDangGetSiteLogDao.selectListByOrderIds(orderIds);
    }
}
