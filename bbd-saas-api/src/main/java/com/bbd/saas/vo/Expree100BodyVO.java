package com.bbd.saas.vo;

/** 封装 body 对象
 * Created by huozhijie on 2016/5/31.
 */
public class Expree100BodyVO {
   /*  例子： param=body{
        "company": "yuantong",
                "number": "12345678",
                "from": "广东深圳",
                "to": "北京朝阳",
                "key": "qWmaQkHp269",
                "parameters": {
            "callbackurl": " http://baidu.com",
                    "salt": "any string",
                    "resultv2": "1"
        }
    }*/
    private String company;//其他快递公司
    private String number;//新运单号
    private String from;// 运单寄出地址
    private  String to;//运单到达地址
    private String key; //推送服务授权密匙(Key)
    private Expree100ParametersVO parameters;//parameters

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Expree100ParametersVO getParameters() {
        return parameters;
    }

    public void setParameters(Expree100ParametersVO parameters) {
        this.parameters = parameters;
    }
}
