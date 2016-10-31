package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.poi.model.SitePOIKeyword;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.LinkedHashMap;

/**
 * Created by Jiwei on 16/4/13.
 */
public class StPOIKeywodDao extends BaseDAO<SitePOIKeyword,ObjectId> {

    public StPOIKeywodDao(LinkedHashMap<String,Datastore> datastore){
        super(datastore);
    }


}
