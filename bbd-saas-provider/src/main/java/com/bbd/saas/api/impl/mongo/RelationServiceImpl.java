package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.RelationService;
import com.bbd.saas.dao.mongo.RelationDao;
import com.bbd.saas.mongoModels.Relation;

/**
 * Created by liyanlei on 2016/10/14.
 * 商户揽件员关连接口实现
 */
public class RelationServiceImpl implements RelationService {
    private RelationDao relationDao;

    public RelationDao getRelationDao() {
        return relationDao;
    }

    public void setRelationDao(RelationDao relationDao) {
        this.relationDao = relationDao;
    }

    @Override
    public Relation findByMechId(String mechId) {
        return this.relationDao.findOne("mechId", mechId);
    }

    @Override
    public void save(Relation relation) {
        this.relationDao.save(relation);
    }
}
