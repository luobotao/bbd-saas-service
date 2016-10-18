package com.bbd.saas.mongoModels;

import com.bbd.saas.enums.SiteSrc;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.SiteTurnDownReasson;
import com.bbd.saas.enums.SiteType;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点表
 * Created by luobotao on 2016/2/23.
 */
@Entity("site")
@Indexes(
        @Index(value = "username", fields = @Field("username"))
)
public class Site implements Serializable {
    private static final long serialVersionUID = -5141450760876419376L;
    @Id
    private ObjectId id;
    private SiteType sitetype;         //站点类型（普通|社会化运力|快递柜）
    private SiteSrc siteSrc = SiteSrc.PUBLIC;// 站点来源
    private String name;         //站点名称
    private String responser;    //负责人
    private String telephone;    //固定电话
    private String email;        //邮箱
    private String province;     //省
    private String city;         //市
    private String area;         //区
    private String address;      //详细地址
    private String licensePic;   //营业执照
    private String username;     //账号即手机号
    private String password;     //密码
    private String areaCode;    //区域（站点）编码
    private String companyId;      //所属公司ID
    private String companyName;      //所属公司名称
    private String companycode;      //所属公司编码
    private String group;
    private SiteStatus status;       //状态
    private int areaFlag;       //配送区域是否有效。1：有效；0：无效
    private Integer upperlimit;     //上线单量,必须为整数
    private Integer lowerlimit;     //下线单量，必须为整数
    private String memo;       //备注
    private SiteTurnDownReasson turnDownReasson;       //驳回原因
    private String otherMessage;       //其他原因
    private String lat;         //纬度
    private String lng;         //经度
    private String deliveryArea;//配送范围
    private Date dateAdd;      //创建时间
    private Date dateUpd;      //更新时间
    private String type;   //1 为分拨站点，0 为普通站点
    @Transient
    private String statusMessage;//JS展示状态
    @Transient
    private String turnDownMessage;//JS展示状态
    @Transient
    private String integralInfo;    //积分信息

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public SiteType getSitetype() {
        return sitetype;
    }

    public void setSitetype(SiteType sitetype) {
        this.sitetype = sitetype;
    }

    public SiteSrc getSiteSrc() {
        if(siteSrc == null){
            return SiteSrc.PUBLIC;
        }
        return siteSrc;
    }

    public void setSiteSrc(SiteSrc siteSrc) {
        this.siteSrc = siteSrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponser() {
        return responser;
    }

    public void setResponser(String responser) {
        this.responser = responser;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public SiteStatus getStatus() {
        return status;
    }

    public void setStatus(SiteStatus status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpd() {
        return dateUpd;
    }

    public void setDateUpd(Date dateUpd) {
        this.dateUpd = dateUpd;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public SiteTurnDownReasson getTurnDownReasson() {
        return turnDownReasson;
    }

    public void setTurnDownReasson(SiteTurnDownReasson turnDownReasson) {
        this.turnDownReasson = turnDownReasson;
    }

    public String getOtherMessage() {
        return otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }

    public String getStatusMessage() {
        if (status == null)
            return "";
        return status.getMessage();
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTurnDownMessage() {
        return turnDownMessage;
    }

    public void setTurnDownMessage(String turnDownMessage) {
        this.turnDownMessage = turnDownMessage;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getAreaFlag() {
        return areaFlag;
    }

    public void setAreaFlag(int areaFlag) {
        this.areaFlag = areaFlag;
    }

    public Integer getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(Integer upperlimit) {
        this.upperlimit = upperlimit;
    }

    public Integer getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(Integer lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntegralInfo() {
        return integralInfo;
    }

    public void setIntegralInfo(String integralInfo) {
        this.integralInfo = integralInfo;
    }


}
