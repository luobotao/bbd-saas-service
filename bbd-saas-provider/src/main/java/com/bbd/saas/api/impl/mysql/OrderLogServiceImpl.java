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
		//未分派
		orderMonitorVO.setNoDispatch(orderMonitorVO.getArrived() - orderMonitorVO.getDispatched());
		return orderMonitorVO;
	}

	/**
	 * 把不同状态的订单数的map转化为orderMonitorVO对象
	 * @param status
	 * @param count
	 * @param orderMonitorVO
	 */
	private  void getOrderMonitorVO(Integer status, Long count, OrderMonitorVO orderMonitorVO){
		System.out.println("status==="+status + " count=="+count);
		//5-已到达站点，6|8-正在派送，9-已签收，7-已滞留，10-已拒收
		if (status == 5){
			orderMonitorVO.setArrived(count);
		}else if (status == 6 || status == 8){
			orderMonitorVO.setDispatched(orderMonitorVO.getDispatched() + count);
		}else if (status == 9){
			orderMonitorVO.setSigned(count);
		}else if (status == 7){
			orderMonitorVO.setRetention(count);
		}else if (status == 10){
			orderMonitorVO.setRejection(count);
		}
	}
}
