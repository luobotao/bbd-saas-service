package com.bbd.saas.api.mysql;

/**
 * 公司Service
 * Created by zuowenhai on 2016/4/16.
 */
public interface OpenUserService {

    /**
     * 获取token
     * @param code 编号
     * @return token
     */
   public String selectTokenByCode(String code);
}
