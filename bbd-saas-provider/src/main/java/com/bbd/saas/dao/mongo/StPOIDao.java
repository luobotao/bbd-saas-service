package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.poi.model.SitePOI;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.LinkedHashMap;

/**
 * Created by Jiwei on 16/4/11.
 */
public class StPOIDao extends BaseDAO<SitePOI,ObjectId> {
    public StPOIDao(LinkedHashMap<String,Datastore> datastore){
        super(datastore);
    }
}
