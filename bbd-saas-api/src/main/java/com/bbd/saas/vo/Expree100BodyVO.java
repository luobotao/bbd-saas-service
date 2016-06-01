package com.bbd.saas.vo;

/**
 * Created by huozhijie on 2016/5/31.
 */
public class Expree100BodyVO {
    private String company;
    private String number;
    private String from;
    private  String to;
    private String key;
    private Expree100ParametersVO parameters;

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
