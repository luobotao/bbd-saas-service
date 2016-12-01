package com.bbd.saas.dao.mysql;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by liyanlei on 2016/6/2.
 */
public interface ComplaintDealDao {

    /**
     * 根据订单号查询投诉处理结果
     * @param mailNum
     * @return 投诉处理结果
     */
    Map<String, Object> selectByMailNum(@Param("mailNum")String mailNum);

    /**
     * 根据订单号查询扣钱情况
     * @param mailNumList 运单号集合
     * @return 投诉处理结果集合
     */
    List<Map<String, Object>> selectMoneyListByMailNums(@Param("mailNumList")List<String> mailNumList);

    /**
     * 根据订单号查询扣分情况
     * @param mailNumList 运单号集合
     * @return 投诉处理结果集合
     */
    List<Map<String, Object>> selectScoreListByMailNums(@Param("mailNumList")List<String> mailNumList);
}
