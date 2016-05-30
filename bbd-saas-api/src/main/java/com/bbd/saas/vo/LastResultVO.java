package com.bbd.saas.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huozhijie on 2016/5/27.
 */
 /*最新查询结果，全量，倒序（即时间最新的在最前）*/
public class LastResultVO {
      private String  message; /*消息体*/

      private    String    state;/*快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、
      6退回、7转单等7个状态，其中4-7需要另外开通才有效. */

      private   String   status ;  /*通讯状态*/


    private  String   condition;/*快递单明细状态标记，暂未实现*/

      private  String   ischeck;/*是否签收标记，明细状态请参考state字段*/

      private  String  com ;/*快递公司编码,一律用小写字母*/

      private String   nu ; /*单号*/

      private List<NewContextVO> data=new ArrayList<NewContextVO>();/*每一条信息详情*/

    public List<NewContextVO> getData() {
        return data;
    }

    public void setData(List<NewContextVO> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }
}
