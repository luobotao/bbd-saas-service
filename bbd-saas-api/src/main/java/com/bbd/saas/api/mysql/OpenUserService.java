package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.PostmanUser;

import java.util.List;

/**
 * 公司Service
 * Created by zuowenhai on 2016/4/16.
 */
public interface OpenUserService {

	/**
     * 获取token
     * @param code
     * @return token
     */
   public String selectTokenByCode(String code);
}
