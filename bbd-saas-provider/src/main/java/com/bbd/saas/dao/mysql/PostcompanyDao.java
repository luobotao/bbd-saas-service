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
    /**
     * 插入一条新公司
     * @param postcompany
     * @return
     */
    int insertCompany(Postcompany postcompany);
}
