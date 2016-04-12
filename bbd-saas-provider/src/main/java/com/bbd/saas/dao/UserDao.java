package com.bbd.saas.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;


/**
 * Created by luobotao on 2016/4/11.
 * 站点用户DAO
 */
@Repository
public class UserDao extends BaseDAO<User, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    UserDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public User findOne(String userName){
    	User user = findOne(userName,"56d013f156f6c3ba9fe959ce");
    	return user;
    }

    /**
     * 获取用户列表信息
     * @param PageModel<User>
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel) {
        PageModel<User> result = new PageModel<User>();
        List<User> orderList = find(createQuery().filter("adminUserId", new ObjectId("56d013f156f6c3ba9fe959cb")).order("dateUpd").offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        result.setDatas(orderList);
        result.setPageNo(2);
        result.setTotalPages(12);
        return result;
    }
    /**
     * 获取用户列表信息
     * @param PageModel<User>
     * @return PageModel<User>
     */
    public List<User> findUserListBySite(String siteId) {
    	Query<User> query = createQuery();
    	/*query.filter("sender.name", "陈建伟");
    	query.filter("sender.phone", "13488884622");
    	query.filter("sender.province", "北京");*/
    	//query.filter("realName", "棒棒糖超级管理员");
    	//分页
    	/*query.order("dateLogin");
    	query.offset(0);
    	query.limit(20);*/
        return  find(query).asList();
    }
}
