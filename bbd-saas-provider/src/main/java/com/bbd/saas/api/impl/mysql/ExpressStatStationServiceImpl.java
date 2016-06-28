package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.ExpressStatStationService;
import com.bbd.saas.dao.mysql.ExpressStatStationDao;
import com.bbd.saas.models.ExpressStatStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 快递员派送运单信息Service实现
 * @author: liyanlei
 * 2016年4月20日下午3:57:47
 */
@Service("expressStatStationService")
@Transactional
public class ExpressStatStationServiceImpl implements ExpressStatStationService {
	@Resource
	private ExpressStatStationDao expressStatStationDao;

	public ExpressStatStationDao getExpressStatStationDao() {
		return expressStatStationDao;
	}

	public void setExpressStatStationDao(ExpressStatStationDao expressStatStationDao) {
		this.expressStatStationDao = expressStatStationDao;
	}

	@Override
	public int insert(ExpressStatStation expressStatStation) {
		return expressStatStationDao.insert(expressStatStation);
	}

	@Override
	public List<ExpressStatStation> findByCompanyIdAndTime(String companyId, String startDate, String endDate) {
		return expressStatStationDao.selectByCompanyIdAndTime(Integer.parseInt(companyId), startDate, endDate);
	}

	@Override
	public List<ExpressStatStation> findByAreaCodeAndTime(String areaCode, String startDate, String endDate) {
		return expressStatStationDao.selectByAreaCodeAndTime(areaCode, startDate, endDate);
	}

	@Override
	public List<ExpressStatStation> findByAreaCodeListAndTime(List<String> areaCodeList, String startDate, String endDate) {
		return expressStatStationDao.selectByAreaCodeListAndTime(areaCodeList, startDate, endDate);
	}
}
