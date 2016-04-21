package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.Postcompany;

import java.util.List;


public interface PostcompanyDao {
    

    List<Postcompany> selectAll();

    Postcompany selectPostmancompanyById(Integer id);
}
