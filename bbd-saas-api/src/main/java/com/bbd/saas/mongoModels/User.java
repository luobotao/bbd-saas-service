package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * User
 * 用户表
 * @author luobotao
 * Created by luobotao on 2016/4/10.
 */
@Entity("user")
@Indexes(
        @Index(value = "loginName", fields = @Field("loginName"))
)
public class User implements Serializable {

    @Id
    private ObjectId id;
    private String loginName;
    private String passWord;
    private String realName;
    private String phone;
    private UserRole role;
    @Reference("site")
    private Site site;
    @Reference("user")
    private User operate;//用户创建者
    private String lastIp;
    private int loginCount;
    private int state;//状态标识 1审核中 2审核通过 3驳回
    private UserStatus userStatus;//状态标识 1启用,0停用 
    private Date dateAdd;//创建时间
    private Date dateLogin;//最后一次登录时间

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateLogin() {
        return dateLogin;
    }

    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }

	public User getOperate() {
		return operate;
	}

	public void setOperate(User operate) {
		this.operate = operate;
	}
}