package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 站点关键词临时对象 -- 用于excel文件导入记录错误信息
 * Created by liyanlei on 2016/5/14.
 */
@Entity("SiteKeyTemp")
public class SiteKeyTemp implements Serializable {
    private static final long serialVersionUID = -5141450760876419376L;
    @Id
    private ObjectId id;
    private String siteId;
    private String siteName;
    private String province;
    private String city;
    private String distict;
    private String keyword;
    private int row;
    private int col;
    private Date errorFlag; //areaCode_loginName_时间
    private Date createDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
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

    public String getDistict() {
        return distict;
    }

    public void setDistict(String distict) {
        this.distict = distict;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Date getErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(Date errorFlag) {
        this.errorFlag = errorFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
