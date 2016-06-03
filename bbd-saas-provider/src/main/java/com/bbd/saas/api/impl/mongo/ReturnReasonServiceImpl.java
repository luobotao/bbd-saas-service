package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.ReturnReasonService;
import com.bbd.saas.dao.mongo.ReturnReasonDao;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.ReturnReason;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class ReturnReasonServiceImpl implements ReturnReasonService {
    private ReturnReasonDao returnReasonDao;

    public ReturnReasonDao getReturnReasonDao() {
        return returnReasonDao;
    }

    public void setReturnReasonDao(ReturnReasonDao returnReasonDao) {
        this.returnReasonDao = returnReasonDao;
    }

    public Key<ReturnReason> save(ReturnReason returnReason) {
        return returnReasonDao.save(returnReason);
    }


	@Override
	public List<ReturnReason> findAll() {
		return returnReasonDao.find().asList();
	}
    @Override
    public ReturnReason findOneByStatus(Integer status) {
        return returnReasonDao.findOne("status", status);
    }

}
