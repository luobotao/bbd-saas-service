package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Postcompany;

import java.util.List;


public interface PostcompanyDao {


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
    /* *
     * 根据公司编号获取公司信息
     * @param companycode 公司编号
     * @return 公司
     */
    Postcompany selectOneByCode(String companycode);
    /**
     * 插入一条新公司
     * @param postcompany
     * @return
     */
    int insertCompany(Postcompany postcompany);

    /**
     * 更新公司
     * @param postcompany
     */
    void updateCompany(Postcompany postcompany);

    /**
     * 根据公司状态获取该状态下的所有公司列表
     * @return
     */
    List<Postcompany> selectAllByStatus(String sta);
}
