package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.ReturnReason;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/11.
 * 站点接口
 */
public interface ReturnReasonService {
     /**
     * 查询所有退货原因
     * @return 原因列表
     */
     List<ReturnReason> findAll();
    /**
     * 根据status查询退货原因
     * @return 原因
     */
    ReturnReason findOneByStatus(Integer status);

    /**
     * 保存
     * @param returnReason 实体
     * @return 保存结果
     */
    Key<ReturnReason> save(ReturnReason returnReason);
}
