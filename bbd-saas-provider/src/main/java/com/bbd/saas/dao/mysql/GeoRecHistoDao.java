package com.bbd.saas.dao.mysql;


import com.bbd.saas.models.GeoRecHisto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeoRecHistoDao {

    void insert(GeoRecHisto geoRecHisto);

    GeoRecHisto findOneByOrderNo(String orderNo);

    /**
     * 根据时间范围查询
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的列表数据
     */
    List<GeoRecHisto> selectByAddrAndDates(@Param("prov")String prov, @Param("city")String city, @Param("area")String area, @Param("startDate")String startDate, @Param("endDate")String endDate);
}
