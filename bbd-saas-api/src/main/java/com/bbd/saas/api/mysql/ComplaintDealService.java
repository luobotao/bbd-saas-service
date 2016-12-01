package com.bbd.saas.api.mysql;

import java.util.List;
import java.util.Map;

/**
 * Created by liyanlei on 2016/11/29.
 */
public interface ComplaintDealService {
    /**
     * 根据订单号查询投诉处理结果
     * @param mailNumList 运单号集合
     * @return 投诉处理结果集合<mailNum, dealResult>
     */
    Map<String, String> findListByMailNums(List<String> mailNumList);
}
