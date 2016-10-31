package com.bbd.poi.model;

import com.bbd.saas.enums.SiteSrc;
import com.bbd.saas.enums.SiteStatusPoi;
import com.bbd.saas.enums.SiteType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.geo.MultiPolygon;
import org.mongodb.morphia.utils.IndexDirection;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liyanlei on 16/10/31.
 */
@Entity("SitePOI")
public class SitePOI  implements Serializable {

    @Id
    public ObjectId id;

    public Date createAt = new Date();

    public Date updateAt = new Date();
    //站点id
    public String siteId;

    //站点所在公司id
    public String companyId;

    //名称
    public String siteName;

    //所在省
    public String province;

    //所在市
    public String city;

    //所在区
    public String district;

    //详细地址
    public String address;

    //服务半径,单位米
    public int radius;

    //服务半径,状态
    public SiteStatusPoi status;

    //站点类型
    public SiteType siteType = SiteType.ORDERNARY;

    //服务对象
    public SiteSrc siteSrc = SiteSrc.PUBLIC;

    //经纬度 ,0 精度 1 纬度
    @Indexed(IndexDirection.GEO2DSPHERE)
    public  double[] location = null;

    @Indexed(IndexDirection.GEO2DSPHERE)
    public MultiPolygon eFences;

}
