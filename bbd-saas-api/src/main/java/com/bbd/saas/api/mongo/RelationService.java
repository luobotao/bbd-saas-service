package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.Relation;

/**
 * Created by liyanlei on 2016/10/14.
 * 商户揽件员关连接口
 */
public interface RelationService {
    /**
     * 根据商户Id查询
     * @param mechId 商户Id
     * @return 关联对象
     */
    Relation findByMechId(String mechId);

    void save(Relation relation);

}
