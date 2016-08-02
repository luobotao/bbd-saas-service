package com.bbd.saas.api.mysql;


/**
 * 返现接口
 * Created by luobotao on 2016/5/25.
 */
public interface IncomeService {

    /**
     * 向司机返现
     * @param driverId 司机Id
     * @param price 价格
     * @param trackNo 运单号
     */
    void driverIncome(int driverId,double price,String trackNo );

    /**
     *
     * @param mailNum
     * @param printDate
     * @param packDate
     * @param driverDate
     * @param stationDate
     * @param deliveryDate
     * @param successDate
     * @param dailyDate
     * @param refuseDate
     * @param changeStationDate
     * @param changeExpressDate
     * @param areaCode
     * @param sitename
     * @param city
     * @param province
     * @param companycode
     * @param companyId
     * @param companyName
     * @param src
     * @param expressSource
     * @param driver_Id
     */
    void expresstomysql(String mailNum, String printDate, String packDate, String driverDate, String stationDate,
                        String deliveryDate, String successDate, String dailyDate, String refuseDate, String changeStationDate,String changeExpressDate,
                        String areaCode,
                        String sitename, String city, String province, String companycode,String companyId,
                        String companyName,String src,String expressSource,  String driver_Id);
}
