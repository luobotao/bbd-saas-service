package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.BbtAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BbtAddressDao {


    /**
     * 根据地址查询所有符合的Bbtaddress
     * @return
     */
    List<BbtAddress> getBbtAddressWithProvince(String province);


    /**
     * 根据地址获取bbtAddress
     * @param id
     * @return
     */
    BbtAddress getBbtAddressWithId(String id);

    /**
     * 修改maxstationcode
     * @param maxstationcode
     * @param code
     */
    void updateMaxstationcodeByCode(@Param("maxstationcode") String maxstationcode, @Param("code") String code);

    BbtAddress findOneWithNameAndTier(String name, String tier);
}
