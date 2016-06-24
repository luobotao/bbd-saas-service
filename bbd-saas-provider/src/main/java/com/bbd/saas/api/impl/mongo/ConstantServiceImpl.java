package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.ConstantService;
import com.bbd.saas.dao.mongo.ConstantDao;
import com.bbd.saas.mongoModels.Constant;
import com.mongodb.WriteResult;
import org.mongodb.morphia.Key;

/**
 * Created by liyanlei on 2016/6/13.
 * constant接口(包裹)
 */
public class ConstantServiceImpl implements ConstantService {
    private ConstantDao constantDao;

    public ConstantDao getConstantDao() {
        return constantDao;
    }
    public void setConstantDao(ConstantDao constantDao) {
        this.constantDao = constantDao;
    }

    @Override
    public Constant findOneByName(String name) {
        if (name == null || "".equals(name)) {
            return null;
        }
        return constantDao.findOne("name", name);
    }

    @Override
    public String findValueByName(String name) {
        if (name == null || "".equals(name)) {
            return null;
        }
        Constant constant = constantDao.findOne("name", name);
        if(constant != null){
           return constant.getValue();
        }
        return null;
    }

    @Override
    public boolean deleteByName(String name) {
        WriteResult result = constantDao.deleteByName(name);
        if(result != null && result.getN() > 0){
            return true;
        }
        return false;
    }

    @Override
    public Key<Constant> save(Constant constant) {
        return constantDao.save(constant);
    }


}
