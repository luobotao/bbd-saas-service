package com.bbd.saas.api.mysql;

import com.bbd.saas.models.Postcompany;

import java.util.List;

/**
 * 公司Service
 * Created by luobotao on 2016/4/16.
 */
public interface PostcompanyService {

    /**
     * 查询所有公司
     * @return
     */
    List<Postcompany> selectAll();

    /**
     * 根据公司ID获取公司信息
     * @param id
     * @return
     */
    Postcompany selectPostmancompanyById(Integer id);
    /**
     * 插入一条新公司
     * @param postcompany
     * @return
     */
    Postcompany insertCompany(Postcompany postcompany);

    /**
     * 更新公司
     * @param postcompany
     */
    Postcompany updateCompany(Postcompany postcompany);
}
