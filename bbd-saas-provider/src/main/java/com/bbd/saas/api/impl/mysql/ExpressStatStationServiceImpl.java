package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.ExpressStatStationService;
import com.bbd.saas.dao.mysql.ExpressStatStationDao;
import com.bbd.saas.models.ExpressStatStation;
import com.bbd.saas.utils.PageModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public List<ExpressStatStation> findByCompanyIdAndTimeBetween(String companyId, String startDate, String endDate) {
		return expressStatStationDao.selectByCompanyIdAndTimeBetween(Integer.parseInt(companyId), startDate, endDate);
	}

	@Override
	public List<ExpressStatStation> findByAreaCodeAndTimeBetween(String areaCode, String startDate, String endDate) {
		return expressStatStationDao.selectByAreaCodeAndTimeBetween(areaCode, startDate, endDate);
	}

	@Override
	public List<ExpressStatStation> findByAreaCodeListAndTimeBetween(List<String> areaCodeList, String startDate, String endDate) {
		return expressStatStationDao.selectByAreaCodeListAndTimeBetween(areaCodeList, startDate, endDate);
	}

	@Override
	public PageModel<ExpressStatStation> findPageByCompanyIdAndTime(String companyId, String tim, PageModel<ExpressStatStation> pageModel) {
		if(pageModel == null){
			pageModel = new PageModel<ExpressStatStation>();
		}
		pageModel.setTotalCount(expressStatStationDao.selectCountByCompanyIdAndTime(Integer.parseInt(companyId), tim));
		if(pageModel.getTotalCount() > 0){
			List<ExpressStatStation> dataList = expressStatStationDao.selectPageByCompanyIdAndTime(Integer.parseInt(companyId), tim, pageModel.getPageNo()* pageModel.getPageSize(), pageModel.getPageSize());
			if((pageModel.getPageNo() + 1) == pageModel.getTotalPages()){//最后一页 -- 需要显示汇总行
				ExpressStatStation summary = expressStatStationDao.selectSummaryByCompanyIdAndTime(Integer.parseInt(companyId), tim);
				summary.setSitename("总计");
				dataList.add(summary);
			}
			pageModel.setDatas(dataList);
		}
		return pageModel;
	}

	@Override
	public List<ExpressStatStation> findByAreaCodeAndTime(String areaCode, String tim) {
		return expressStatStationDao.selectByAreaCodeAndTime(areaCode,tim);
	}

	@Override
	public List<ExpressStatStation> findByCompanyIdAndTime(String companyId, String tim) {
		List<ExpressStatStation> dataList = expressStatStationDao.selectPageByCompanyIdAndTime(Integer.parseInt(companyId), tim, 0, 0);
		if(dataList != null && dataList.size() > 0){//需要显示汇总行
			ExpressStatStation summary = expressStatStationDao.selectSummaryByCompanyIdAndTime(Integer.parseInt(companyId), tim);
			summary.setSitename("总计");
			dataList.add(summary);
		}
		return dataList;
	}

	@Override
	public ExpressStatStation findSummaryByCompanyIdAndTime(String companyId, String tim) {
		return expressStatStationDao.selectSummaryByCompanyIdAndTime(Integer.parseInt(companyId), tim);
	}

	@Override
	public Map<String, ExpressStatStation> findByAreaCodeListAndTime(List<String> areaCodeList, String time) {
		List<ExpressStatStation> expressStatStationList = expressStatStationDao.selectByAreaCodeListAndTime(areaCodeList, time);
		if(expressStatStationList != null){
			return null;
		}
		Map<String, ExpressStatStation> essMap = new HashMap<String, ExpressStatStation>();
		for (ExpressStatStation ess : expressStatStationList){
			essMap.put(ess.getAreacode(), ess);
		}
		return essMap;
	}

	@Override
	public Map<String, ExpressStatStation> findMapByCompanyIdAndTime(String companyId, String tim) {
		List<ExpressStatStation> expressStatStationList = expressStatStationDao.selectByCompanyIdAndTime(companyId, tim);
		if(expressStatStationList != null){
			return null;
		}
		Map<String, ExpressStatStation> essMap = new HashMap<String, ExpressStatStation>();
		for (ExpressStatStation ess : expressStatStationList){
			essMap.put(ess.getAreacode(), ess);
		}
		return essMap;
	}
}
