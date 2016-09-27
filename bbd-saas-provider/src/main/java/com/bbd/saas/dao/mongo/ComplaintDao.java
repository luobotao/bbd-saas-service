package com.bbd.saas.dao.mongo;


import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.AppealStatus;
import com.bbd.saas.enums.ComplaintStatus;
import com.bbd.saas.enums.PunishReason;
import com.bbd.saas.mongoModels.Complaint;
import com.bbd.saas.utils.DateBetween;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.ComplaintQueryVO;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by liyanlei on 2016/9/27.
 * 投诉Dao
 */
@Repository
public class ComplaintDao extends BaseDAO<Complaint, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(ComplaintDao.class);

    ComplaintDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    /**
     * 带查询条件去检索订单
     *
     * @param pageModel        分页对象
     * @param complaintQueryVO 查询条件对象
     * @return 一页的数据+分页信息
     */
    public PageModel<Complaint> selectPageDatas(PageModel<Complaint> pageModel, ComplaintQueryVO complaintQueryVO) {
        Query<Complaint> query = this.getQuery(complaintQueryVO);
        pageModel.setTotalCount(count(query));//数据总条数
        query.order("-dateAdd");//投诉时间倒叙排序
        List<Complaint> complaintList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        pageModel.setDatas(complaintList);//当前页的数据
        return pageModel;
    }

    private Query<Complaint> getQuery(ComplaintQueryVO complaintQueryVO) {
        Query<Complaint> query = createQuery();
        if (complaintQueryVO != null) {
            if (StringUtils.isNotBlank(complaintQueryVO.mailNum)) {//运单号
                query.criteria("mailNum").containsIgnoreCase(complaintQueryVO.mailNum);
            }
            if (StringUtils.isNotBlank(complaintQueryVO.between)) {//投诉时间
                DateBetween dateBetween = new DateBetween(complaintQueryVO.between);
                query.filter("dateAdd >=", dateBetween.getStart());
                query.filter("dateAdd <=", dateBetween.getEnd());
            }
            if (StringUtils.isNotBlank(complaintQueryVO.areaCode)) {//站点编号
                query.filter("areaCode", complaintQueryVO.areaCode);
            }
            if (complaintQueryVO.complaintStatus != null && complaintQueryVO.complaintStatus != -1) {//投诉状态
                query.filter("complaintStatus", ComplaintStatus.status2Obj(complaintQueryVO.complaintStatus));
            }
            if (complaintQueryVO.appealStatus != null && complaintQueryVO.appealStatus != -1) {//申诉状态
                query.filter("appealStatus", AppealStatus.status2Obj(complaintQueryVO.appealStatus));
            }
            if (complaintQueryVO.reason != null && complaintQueryVO.reason != -1) {//投诉理由
                query.filter("reason", PunishReason.status2Msg(complaintQueryVO.reason));
            }
        }
        return query;
    }
}
