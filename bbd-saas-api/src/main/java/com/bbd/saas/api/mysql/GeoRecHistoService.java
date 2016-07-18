package com.bbd.saas.api.mysql;


import com.bbd.saas.models.GeoRecHisto;

/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface GeoRecHistoService {


    void insert(GeoRecHisto geoRecHisto);

    GeoRecHisto findOneByOrderNo(String orderNo);
}
