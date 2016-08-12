package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.ToOtherSiteLogService;
import com.bbd.saas.dao.mongo.ToOtherSiteLogDao;
import com.bbd.saas.mongoModels.ToOtherSiteLog;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class ToOtherSiteLogServiceImpl implements ToOtherSiteLogService {
    private ToOtherSiteLogDao toOtherSiteLogDao;

	public ToOtherSiteLogDao getToOtherSiteLogDao() {
		return toOtherSiteLogDao;
	}

	public void setToOtherSiteLogDao(ToOtherSiteLogDao toOtherSiteLogDao) {
		this.toOtherSiteLogDao = toOtherSiteLogDao;
	}

	@Override
	public long countByFromAreaCodeAndTime(String fromAreaCode, String between) {
		return toOtherSiteLogDao.countByFromAreaCodeAndTime(fromAreaCode, between);
	}

	@Override
	public Key<ToOtherSiteLog> save(ToOtherSiteLog toOtherSiteLog) {
		return toOtherSiteLogDao.save(toOtherSiteLog);
	}

	@Override
	public long countByFromAreaCodesAndTime(List<String> fromAreaCodeList, String between) {
		return toOtherSiteLogDao.countByFromAreaCodesAndTime(fromAreaCodeList, between);
	}
}
