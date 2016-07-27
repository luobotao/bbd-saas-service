package com.bbd.saas.utils;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.vo.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luobotao on 16/6/29.
 */
public class SiteCommon {
    /**
     * 获取站点下拉列表数据
     * @param siteService siteService对象
     * @param companyId 公司Id
     * @return 站点下拉列表数据（List<areaCode,name>）
     */
    public static  List<Option> getSiteOptions(SiteService siteService, String companyId){
        List<SiteStatus> statusList = new ArrayList<SiteStatus>();
        statusList.add(SiteStatus.APPROVE);
        statusList.add(SiteStatus.INVALID);
        return siteService.findByCompanyIdAndAddress(companyId, null, null, null, null, statusList);
    }
}
