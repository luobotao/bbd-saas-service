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
     * @return 公司集合
     */
    List<Postcompany> selectAll();
    /**
     * 根据公司状态获取该状态下的所有公司列表
     * @param sta  状态
     * @return 公司集合
     */
    List<Postcompany>selectAllByStatus(String sta) ;

    /**
     * 根据公司ID获取公司信息
     * @param id 公司ID
     * @return 公司
     */
    Postcompany selectPostmancompanyById(Integer id);
    /**
     * 插入一条新公司
     * @param postcompany 公司
     * @return 公司
     */
    Postcompany insertCompany(Postcompany postcompany);

    /**
     * 更新公司
     * @param postcompany 公司
     * @return 公司
     */
    Postcompany updateCompany(Postcompany postcompany);
}
