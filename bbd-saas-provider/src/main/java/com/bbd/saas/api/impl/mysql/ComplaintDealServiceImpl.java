package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.ComplaintDealService;
import com.bbd.saas.dao.mysql.ComplaintDealDao;
import com.bbd.saas.utils.Numbers;
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
        List<Map<String, Object>> complaintDealVOList = this.complaintDealDao.selectListByMailNums(mailNumList);
        if(complaintDealVOList == null || complaintDealVOList.isEmpty()){
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        StringBuffer dealSB = null;
        int amount;
        BigDecimal score;
        for(Map<String, Object> dealMap : complaintDealVOList){
            dealSB = new StringBuffer();
            amount = (Integer)dealMap.get("amount");
            score = (BigDecimal)dealMap.get("score");
            if(amount != 0){
                dealSB.append(Numbers.intToStringWithDiv(amount, 100));
                dealSB.append("元");
            }
            if(score.doubleValue() != 0){
                if(dealSB.length() > 0){
                    dealSB.append("，");
                }
                dealSB.append("-");
                dealSB.append(score);
                dealSB.append("分");
            }
            map.put((String)dealMap.get("mailNum"), dealSB.toString());
        }
        return map;
    }
}
