package com.bbd.saas.dao.mysql;


import com.bbd.saas.models.GeoRecHisto;

public interface GeoRecHistoDao {

    void insert(GeoRecHisto geoRecHisto);

    GeoRecHisto findOneByOrderNo(String orderNo);
}
