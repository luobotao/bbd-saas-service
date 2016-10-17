package com.bbd.saas.timer;

import ch.hsr.geohash.GeoHash;
import com.bbd.poi.api.Geo;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mysql.GeoRecHistoService;
import com.bbd.saas.models.GeoRecHisto;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.vo.Reciever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 运单push逻辑
 */
@Component("geoRecHistoJob")
public class GeoRecHistoJob {
    public static final Logger logger = LoggerFactory.getLogger(GeoRecHistoJob.class);
    @Autowired
    OrderService orderService;
    @Autowired
    Geo geo;
    @Autowired
    private GeoRecHistoService geoRecHistoService;

    //每天凌晨（12点半）跑一次
    @Scheduled(cron = "0 30 0 * * ?")
    public void doJobWithPushAllTrade() {
        Date date = Dates.getNextDay(new Date(), -1);
        logger.info("定时处理收件人信息" + date);
        logger.info("当前处理时间：" + Dates.formatDate(date));
        //data 的格式为yyyy-MM-dd
        if (date != null) {
            List<Order> orderList = orderService.findByDateAdd(date);
            logger.info(String.format("日期：%s 处理订单数：%s", Dates.formatDate2(date), orderList.size()));
            dealGeoRecWithOrder(orderList);
        }
        logger.info("geo deal date:" + date + "cover Reciver address to geo finish");
        logger.info(" reduceGeoRecHisto success");
        logger.info("cover Reciver address to geo finish");
    }

    private void dealGeoRecWithOrder(List<Order> orderList) {
        for (Order order : orderList) {
            try {
                Reciever reciever = order.getReciever();
                if (reciever != null) {
                    //将订单信息入库
                    GeoRecHisto geoRecHisto = geoRecHistoService.findOneByOrderNo(order.getOrderNo());
                    if (geoRecHisto == null) {
                        geoRecHisto = new GeoRecHisto();
                        geoRecHisto.setOrderNo(order.getOrderNo());
                        geoRecHisto.province = reciever.getProvince();
                        geoRecHisto.city = reciever.getCity();
                        geoRecHisto.area = reciever.getArea();
                        geoRecHisto.address = reciever.getAddress();
                        geoRecHisto.src = order.getSrc().getMessage();
                        geoRecHisto.dateAdd = Dates.formatDate2(order.getDateAdd());
                        MapPoint mapPoint = geo.getGeoInfo(reciever.getProvince() + reciever.getCity() + reciever.getArea() + reciever.getAddress());
                        if (mapPoint != null) {
                            geoRecHisto.setLat(mapPoint.getLat());
                            geoRecHisto.setLng(mapPoint.getLng());
                            GeoHash geoHash = GeoHash.withCharacterPrecision(geoRecHisto.getLat(), geoRecHisto.getLng(), 9);
                            geoRecHisto.setGeoStr(geoHash.toBase32());
                            geoRecHistoService.insert(geoRecHisto);
                            logger.info("订单：" + order.getOrderNo() + "收件人地址转换完成");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("[error]订单：" + order.getOrderNo() + "收件人地址转换异常");
                continue;
            }
        }
    }

}