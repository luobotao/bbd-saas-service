package com.bbd.saas.api.mysql;


import com.bbd.saas.models.GeoRecHisto;

import java.util.List;

/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface GeoRecHistoService {


    void insert(GeoRecHisto geoRecHisto);

    GeoRecHisto findOneByOrderNo(String orderNo);

    /**
     * 根据时间范围查询
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的列表数据
     */
    List<GeoRecHisto> findByDates(String prov, String city, String area, String startDate, String endDate);
}
