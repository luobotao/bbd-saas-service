package com.bbd.poi.model;

/**
 * Created by Jiwei on 16/4/8.
 */

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.Date;


@Entity("SitePOIKeyword")
public class SitePOIKeyword implements Serializable {
    @Id
    public ObjectId id;
    public Date createAt = new Date();
    public Date updateAt = new Date();

    public String siteId;

    public String province;

    public String city;

    public String district;

    public String keyword;

}