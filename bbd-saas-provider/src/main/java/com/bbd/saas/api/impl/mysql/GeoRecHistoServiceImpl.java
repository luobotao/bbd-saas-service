package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.GeoRecHistoService;
import com.bbd.saas.dao.mysql.GeoRecHistoDao;
import com.bbd.saas.models.GeoRecHisto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 返现Service实现
 * Created by luobotao on 2016/5/25.
 */
@Service("geoRecHistoService")
@Transactional
public class GeoRecHistoServiceImpl implements GeoRecHistoService {
    @Resource
    private GeoRecHistoDao geoRecHistoDao;

    public GeoRecHistoDao getGeoRecHistoDao() {
        return geoRecHistoDao;
    }


    public void setGeoRecHistoDao(GeoRecHistoDao geoRecHistoDao) {
        this.geoRecHistoDao = geoRecHistoDao;
    }

    @Override
    public void insert(GeoRecHisto geoRecHisto) {
        geoRecHistoDao.insert(geoRecHisto);
    }

    @Override
    public GeoRecHisto findOneByOrderNo(String orderNo) {
        return geoRecHistoDao.findOneByOrderNo(orderNo);
    }

    @Override
    public List<GeoRecHisto> findByAddrAndDates(String prov, String city, String area, String startDate, String endDate, Integer pageIndex, Integer pageSize) {
        return geoRecHistoDao.selectPageByAddrAndDates(prov, city, area, startDate, endDate, pageIndex*pageSize, pageSize);
    }

    @Override
    public int findCountByAddrAndDates(String prov, String city, String area, String startDate, String endDate) {
        return geoRecHistoDao.selectCountByAddrAndDates(prov, city, area, startDate, endDate);
    }
}
