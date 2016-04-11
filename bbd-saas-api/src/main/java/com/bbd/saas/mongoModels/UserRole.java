package com.bbd.saas.mongoModels;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * User
 * 用户角色表
 * @author luobotao
 * Created by luobotao on 2016/4/10.
 */
@Entity("userRole")
@Indexes(
        @Index(value = "userName", fields = @Field("userName"))
)
public class UserRole implements Serializable {

    @Id
    private ObjectId id;
    private String roleName;
    @Embedded
    private Authority authority;
    private Date dateAdd;//创建时间
    private Date dateUpdate;//修改时间

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @Embedded
    public class Authority{

    }

}

