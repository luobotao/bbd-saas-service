package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Postcompany;

import java.util.List;

/**
 * 公司Service
 * Created by luobotao on 2016/4/16.
 */
public interface PostcompanyService {

    List<Postcompany> selectAll();
}
