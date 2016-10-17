package com.bbd.saas.dao.mysql;


import com.bbd.saas.models.GeoRecHisto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GeoRecHistoDao {

    void insert(GeoRecHisto geoRecHisto);

    GeoRecHisto findOneByOrderNo(String orderNo);

    /**
     * 根据地址和时间范围分页查询
     * @param prov 省
     * @param city 市
     * @param area 区|县
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param skip 跳过的条数
     * @param step 查询的条数
     * @return 符合条件的列表数据
     */
    List<GeoRecHisto> selectPageByAddrAndDates(@Param("prov")String prov, @Param("city")String city, @Param("area")String area, @Param("startDate")String startDate, @Param("endDate")String endDate, @Param("skip")int skip, @Param("step")int step);
    /**
     * 根据地址和时间范围查询数据条数
     * @param prov 省
     * @param city 市
     * @param area 区|县
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 符合条件的数据条数
     */
    int selectCountByAddrAndDates(@Param("prov")String prov, @Param("city")String city, @Param("area")String area, @Param("startDate")String startDate, @Param("endDate")String endDate);

}
