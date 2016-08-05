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
     * 根据地址和时间范围分页查询
     * @param prov 省
     * @param city 市
     * @param area 区|县
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pageIndex  当前页
     * @param pageSize 每页条数
     * @return
     */
    List<GeoRecHisto> findByAddrAndDates(String prov, String city, String area, String startDate, String endDate, Integer pageIndex, Integer pageSize);

    /**
     * 根据地址和时间范围查询数据条数
     * @param prov 省
     * @param city 市
     * @param area 区|县
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的数据条数
     */
    int findCountByAddrAndDates(String prov, String city, String area, String startDate, String endDate);

}
