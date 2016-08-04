package com.bbt.demo.provider;

import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.api.mongo.OrderService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mysql.IncomeService;
import com.bbd.saas.enums.ExpressExchangeStatus;
import com.bbd.saas.enums.ExpressStatus;
import com.bbd.saas.enums.Srcs;
import com.bbd.saas.mongoModels.ExpressExchange;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.Dates;
import com.bbd.saas.utils.ExcelReader;
import com.bbd.saas.vo.Express;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OrderToOtherCompanyTest {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderToOtherCompanyTest.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    ExpressExchangeService expressExchangeService;
    @Autowired
    SiteService siteService;
    @Autowired
    IncomeService incomeService;


    @Test
    public void testReadExcel() throws Exception {
//		readOrderInfo(0);//sheet0
        readOrderInfoToStat(0);//sheet0
    }

    private void readOrderInfoToStat(int sheetIndex) throws IOException {
        String fileToBeRead = "/Users/Axu/Desktop/7.xlsx";
        Workbook wb;
        Sheet sheet;
        // 对读取Excel表格内容测试

        if (fileToBeRead.indexOf(".xlsx") > -1) {
            wb = new XSSFWorkbook(new FileInputStream(fileToBeRead));
        } else {
            wb = new HSSFWorkbook(new FileInputStream(fileToBeRead));
        }
        sheet = wb.getSheetAt(sheetIndex);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        System.out.println("行数：" + rowNum);
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            String mailNum = ExcelReader.getStringCellValue(row.getCell(1));
            Order order = orderService.findOneByMailNum(mailNum);
            if (order != null) {
                String areaCode = order.getAreaCode();
                String sitename = "";
                String city = "";
                String province = "";
                String companycode = "";
                String companyId = "";
                String companyName = "";
                String driver_Id = "";

                Site site = siteService.findSiteByAreaCode(order.getAreaCode());


                if (site != null) {
                    sitename = site.getName();
                    city = site.getCity();
                    province = site.getProvince();
                    companycode = site.getCompanycode();
                    companyId = site.getCompanyId();
                    companyName = site.getCompanyName();

                    String expressSource = order.getSrc().toString();
                    List<Express> expressList = order.getExpresses();
                    String printDate = "";
                    String packDate = "";
                    String driverDate = "";
                    String stationDate = "";
                    String deliveryDate = "";
                    String successDate = "";
                    String dailyDate = "";
                    String refuseDate = "";
                    String changeStationDate = "";
                    String changeExpressDate = "";
                    if (expressList != null) {
                        for (Express e : expressList) {
                            String dateTemp = Dates.formatDate(e.getDateAdd(), "yyyy-MM-dd HH:mm:ss");
                            if (e.getRemark().indexOf("订单已打印") >= 0) {
                                printDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("订单已打包") >= 0) {
                                packDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("司机已取货") >= 0) {
                                driverDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("正在分派配送员") >= 0) {
                                stationDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("派件员正在配送") >= 0) {
                                deliveryDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("您的订单已送达") >= 0) {
                                successDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("滞留") >= 0) {
                                dailyDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("拒收") >= 0) {
                                refuseDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("正在转送到") >= 0) {
                                changeStationDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("已转发") >= 0) {
                                changeExpressDate = dateTemp;
                            }
                            if (e.getRemark().indexOf("签收") >= 0) {
                                successDate = dateTemp;
                            }
                        }
                    }

                    String dateTemp = Dates.formatDate(order.getDateUpd(), "yyyy-MM-dd HH:mm:ss");
                    if (order.getExpressStatus() == ExpressStatus.Success) {
                        successDate = dateTemp;
                    }
                    if (order.getExpressStatus() == ExpressStatus.Delay) {
                        dailyDate = dateTemp;
                    }
                    if (order.getExpressStatus() == ExpressStatus.Refuse) {
                        refuseDate = dateTemp;
                    }

                    expresstomysql(mailNum, printDate, packDate, driverDate, stationDate,
                            deliveryDate, successDate, dailyDate, refuseDate, changeStationDate, changeExpressDate,
                            areaCode,
                            sitename, city, province, companycode, companyId,
                            companyName, expressSource, driver_Id);
                }
            }
        }
    }

    public void expresstomysql(String mailNum, String printDate, String packDate, String driverDate, String stationDate,
                               String deliveryDate, String successDate, String dailyDate, String refuseDate, String changeStationDate, String changeExpressDate,
                               String areaCode,
                               String sitename, String city, String province, String companycode, String companyId,
                               String companyName, String expressSource, String driver_Id) {
        String src = "BBD";
        incomeService.expresstomysql(mailNum, printDate, packDate, driverDate, stationDate,
                deliveryDate, successDate, dailyDate, refuseDate, changeStationDate, changeExpressDate,
                areaCode,
                sitename, city, province, companycode, companyId,
                companyName, src, expressSource, driver_Id);
    }


    private void readOrderInfo(int sheetIndex) throws IOException {
        String fileToBeRead = "/Users/Axu/Desktop/7.xlsx";
        Workbook wb;
        Sheet sheet;
        // 对读取Excel表格内容测试

        if (fileToBeRead.indexOf(".xlsx") > -1) {
            wb = new XSSFWorkbook(new FileInputStream(fileToBeRead));
        } else {
            wb = new HSSFWorkbook(new FileInputStream(fileToBeRead));
        }
        sheet = wb.getSheetAt(sheetIndex);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        System.out.println("行数：" + rowNum);
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            String mailNum = ExcelReader.getStringCellValue(row.getCell(1));
            Order order = orderService.findOneByMailNum(mailNum);
            if (order != null && order.getExpressStatus() != ExpressStatus.Success) {
                User user = new User();
                user.setLoginName("admin");
                user.setRealName("admin");
                user.setLoginName("");
                Site site = siteService.findSiteByAreaCode(order.getAreaCode());
                user.setSite(site == null ? new Site() : site);

                Date dateTemp;
                String packageDate = ExcelReader.getDateCellValue(row.getCell(26));//打包时间
                dateTemp = Dates.parseDate(packageDate, "yyyy-MM-dd HH:mm:ss");
                order.setDateUpd(dateTemp);
                addOrderExpress(ExpressStatus.Packed, order, user, "订单已打包，待司机取货。", dateTemp);
                addExpressExchange(order, user);

                String driverGetDate = ExcelReader.getDateCellValue(row.getCell(27));//司机取货时间
                dateTemp = Dates.parseDate(driverGetDate, "yyyy-MM-dd HH:mm:ss");
                order.setDateDriverGeted(dateTemp);
                order.setDateUpd(dateTemp);
                addOrderExpress(ExpressStatus.DriverGeted, order, user, "司机已取货，正在运输。", dateTemp);
                addExpressExchange(order, user);

                String arriveDate = ExcelReader.getDateCellValue(row.getCell(29));//到站时间
                dateTemp = Dates.parseDate(arriveDate, "yyyy-MM-dd HH:mm:ss");
                order.setDateUpd(dateTemp);
                order.setDateArrived(dateTemp);
                order.setDateMayArrive(dateTemp);
                //增加物流信息(到站)
                addOrderExpress(ExpressStatus.ArriveStation, order, user, "订单已送达【" + user.getSite().getName() + "】，正在分派配送员", dateTemp);
                addExpressExchange(order, user);


                String deliveryDate = ExcelReader.getDateCellValue(row.getCell(30));//分派时间
                dateTemp = Dates.parseDate(deliveryDate, "yyyy-MM-dd HH:mm:ss");
                order.setDateUpd(dateTemp);
                addOrderExpress(ExpressStatus.Delivering, order, user, "派件员正在配送", dateTemp);
                addExpressExchange(order, user);

                String successDate = ExcelReader.getDateCellValue(row.getCell(31));//签收时间
                dateTemp = Dates.parseDate(successDate, "yyyy-MM-dd HH:mm:ss");
                order.setDateUpd(dateTemp);
                addOrderExpress(ExpressStatus.Success, order, user, "您的订单已完成签收", dateTemp);
                addExpressExchange(order, user);

                orderService.save(order);
            }
        }
    }

    public void addExpressExchange(Order order, User user) {
        if (Srcs.DANGDANG.equals(order.getSrc()) || Srcs.PINHAOHUO.equals(order.getSrc()) || Srcs.DDKY.equals(order.getSrc())) {
            ExpressExchange expressExchange = new ExpressExchange();
            expressExchange.setOperator(user.getRealName());
            expressExchange.setStatus(ExpressExchangeStatus.waiting);
            expressExchange.setPhone(user.getLoginName());
            expressExchange.setOrder(order.coverOrderVo());
            expressExchange.setDateAdd(new Date());
            expressExchangeService.save(expressExchange);
        }
    }

    /**
     * 增加订单物流信息
     *
     * @param expressStatus 物流状态
     * @param order         订单
     * @param user          当前用户
     * @param remark        物流信息
     */
    public static void addOrderExpress(ExpressStatus expressStatus, Order order, User user, String remark, Date date) {
        //更新物流状态
        order.setExpressStatus(expressStatus);
        //更新物流信息
        List<Express> expressList = order.getExpresses();
        if (expressList == null) {
            expressList = new ArrayList<Express>();
        }
        Express express = new Express();
        express.setDateAdd(date);
        express.setRemark(remark);
        express.setLat(user.getSite().getLat());
        express.setLon(user.getSite().getLng());
        boolean expressIsNotAdd = true;//防止多次添加
        //检查是否添加过了
        if (!expressList.isEmpty()) {
            Express express1 = expressList.get(expressList.size() - 1);
            if (express.getRemark().equals(express1.getRemark())) {
                expressIsNotAdd = false;
            }
        }
        if (expressIsNotAdd) {//防止多次添加
            expressList.add(express);
            order.setExpresses(expressList);
        }
    }


}
