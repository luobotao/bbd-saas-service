package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.Constant;
import org.mongodb.morphia.Key;

/**
 * Created by liyanlei on 2016/6/23.
 * 常量接口
 */
public interface ConstantService {
    /**
     * 根据name查询常量对象
     * @param name  常量名称
     * @return  常量对象
     */
    Constant findOneByName(String name);
    /**
     * 根据name查询常量值
     * @param name  常量名称
     * @return  常量值
     */
    String findValueByName(String name);
    /**
     * 保存常量对象
     * @param trade 常量
     * @return Key<Trade>保存结果
     */
    Key<Constant> save(Constant trade);
    /**
     * 根据常量名称删除
     * @param name 常量名称
     */
    boolean deleteByName(String name);


}
