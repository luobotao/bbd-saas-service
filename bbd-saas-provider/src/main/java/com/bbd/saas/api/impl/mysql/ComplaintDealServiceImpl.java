package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.ComplaintDealService;
import com.bbd.saas.dao.mysql.ComplaintDealDao;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyanlei on 2016/11/29.
 */
@Service("complaintDealService")
@Transactional
public class ComplaintDealServiceImpl implements ComplaintDealService {
    @Resource
    private ComplaintDealDao complaintDealDao;

    public ComplaintDealDao getComplaintDealDao() {
        return complaintDealDao;
    }

    public void setComplaintDealDao(ComplaintDealDao complaintDealDao) {
        this.complaintDealDao = complaintDealDao;
    }

    @Override
    public Map<String, String> findListByMailNums(List<String> mailNumList) {
        Map<String, String> map = new HashMap<String, String>();
        if(mailNumList == null || mailNumList.isEmpty()){
            return map;
        }
        List<Map<String, Object>> moneyList = this.complaintDealDao.selectMoneyListByMailNums(mailNumList);
        List<Map<String, Object>> scoreList = this.complaintDealDao.selectScoreListByMailNums(mailNumList);
        Map<String, String> moneyMap = new HashMap<String, String>();
        Map<String, String> scoreMap = new HashMap<String, String>();
        StringBuffer dealSB = null;
        //扣钱
        if((moneyList != null && !moneyList.isEmpty()) ){
            for(Map<String, Object> dealMap : moneyList){
                dealSB = new StringBuffer();
                dealSB.append(Numbers.intToStringWithDiv((Integer)dealMap.get("amount"), 100));
                dealSB.append("元");
                moneyMap.put((String)dealMap.get("mailNum"), dealSB.toString());
            }
        }
        //扣分
        if((scoreList != null && !scoreList.isEmpty()) ){
            for(Map<String, Object> dealMap : scoreList){
                dealSB = new StringBuffer();
                dealSB.append(dealMap.get("score") == null ? "0.0" : dealMap.get("score"));
                dealSB.append("分");
                scoreMap.put((String)dealMap.get("mailNum"), dealSB.toString());
            }
        }
        String result = null;
        for(String mailNum : mailNumList){
            result = moneyMap.get(mailNum);
            if(result == null){
                dealSB = new StringBuffer("0.0元");
            }else{
                dealSB = new StringBuffer(result);
            }
            dealSB.append("，-");
            //拼接分
            result = scoreMap.get(mailNum);
            if(result == null){
                dealSB.append("0.0分");
            }else{
                dealSB.append(result.replace("0分", "分"));
            }
            map.put(mailNum, dealSB.toString());
        }
        return map;
    }
}
