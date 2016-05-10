package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.OrderLogService;
import com.bbd.saas.dao.mysql.OrderLogDao;
import com.bbd.saas.models.OrderLog;
import com.bbd.saas.vo.OrderMonitorVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description: 快递员派送运单信息Service实现
 * @author: liyanlei
 * 2016年4月20日下午3:57:47
 */
@Service("orderLogService")
@Transactional
public class OrderLogServiceImpl implements OrderLogService {
	@Resource
	private OrderLogDao orderLogDao;

	public OrderLogDao getOrderLogDao() {
		return orderLogDao;
	}

	public void setOrderLogDao(OrderLogDao orderLogDao) {
		this.orderLogDao = orderLogDao;
	}

	@Override
	public int insert(OrderLog orderLog) {
		return orderLogDao.insert(orderLog);
	}

	@Override
	public OrderMonitorVO statisticOrderNum(String areaCode, String startDate, String endDate) {
		List<Map<String, Object>> dataList = orderLogDao.selectCountByAreaCodeAndTime(areaCode, startDate, endDate);
		if (dataList == null){
			return null;
		}
		OrderMonitorVO orderMonitorVO = new OrderMonitorVO();
		//
		for (Map<String, Object> map : dataList){
			getOrderMonitorVO((Integer) map.get("status"), (Long) map.get("num"), orderMonitorVO);
		}
		return orderMonitorVO;
	}

	/**
	 * 把不同状态的订单数的map转化为orderMonitorVO对象
	 * @param status
	 * @param count
	 * @param orderMonitorVO
	 */
	private  void getOrderMonitorVO(Integer status, Long count, OrderMonitorVO orderMonitorVO){
		//0-未到达站点，1-已到达站点，2-正在派送，3-已签收，4-已滞留，5-已拒收，6-转站, 7-已丢失,8-已取消
		if(status == 0){
			orderMonitorVO.setNoArrive(count);
		}else if (status == 1){
			orderMonitorVO.setArrived(count);
		}else if (status == 2){
			orderMonitorVO.setDispatched(count);
		}else if (status == 3){
			orderMonitorVO.setSigned(count);
		}else if (status == 4){
			orderMonitorVO.setRetention(count);
		}else if (status == 5){
			orderMonitorVO.setRejection(count);
		}
	}
}
