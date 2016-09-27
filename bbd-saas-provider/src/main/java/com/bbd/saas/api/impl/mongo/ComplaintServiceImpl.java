package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.ComplaintService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.dao.mongo.ComplaintDao;
import com.bbd.saas.mongoModels.Complaint;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.ComplaintQueryVO;
import com.bbd.saas.vo.entity.ComplaintVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanlei on 2016/9/27.
 * 投诉接口
 */
public class ComplaintServiceImpl implements ComplaintService {
    private ComplaintDao complaintDao;
    @Autowired
    private UserService userService;

    public ComplaintDao getComplaintDao() {
        return complaintDao;
    }

    public void setComplaintDao(ComplaintDao complaintDao) {
        this.complaintDao = complaintDao;
    }

    @Override
    public PageModel<ComplaintVO> findPageDatas(PageModel<Complaint> pageModel, ComplaintQueryVO complaintQueryVO) {
        PageModel<ComplaintVO> pageModelVO = new PageModel<ComplaintVO>();
        pageModelVO.setPageNo(pageModel.getPageNo());
        pageModel = this.complaintDao.selectPageDatas(pageModel, complaintQueryVO);
        //设置派件员快递和电话
        if(pageModel != null && pageModel.getDatas() != null){
            pageModelVO.setTotalCount(pageModel.getTotalCount());
            List<Complaint> dataList = pageModel.getDatas();
            User courier = null;
            List<ComplaintVO> dataVoList = new ArrayList<ComplaintVO>();
            for(Complaint complaint : dataList){
                ComplaintVO complaintVO = new ComplaintVO();
                BeanUtils.copyProperties(complaint,complaintVO);//待显示属性
                complaintVO.setComplaintStatusMsg(complaintVO.getComplaintStatus().getMessage());
                complaintVO.setAppealStatusMsg(complaintVO.getAppealStatus().getMessage());
                courier = userService.findUserByLoginName(complaint.getPostmanPhone());
                if(courier != null){//被投诉人姓名
                    complaintVO.setRespondent(courier.getRealName());
                }
                dataVoList.add(complaintVO);
            }
            pageModelVO.setDatas(dataVoList);
        }
        return pageModelVO;
    }
}
