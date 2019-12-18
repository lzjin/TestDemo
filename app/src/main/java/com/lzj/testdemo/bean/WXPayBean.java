package com.lzj.testdemo.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/12.
 */

public class WXPayBean implements Serializable {

    /**
     * package : Sign=WXPay
     * appid : wxa0c4da2b17dd9c8c
     * outTradeNo : 873067993339265024
     * sign : D07F6525628FF6C96742F6127F7ECFD3
     * partnerid : 1482006732
     * prepayid : wx2017061210342313520f617b0631712346
     * noncestr : 8i9hddnmipu40m3w3hsssqfx3k7u6w92
     * timestamp : 1497234853
     */

    @com.google.gson.annotations.SerializedName("package")
    private String packageX;
    private String appid;
    private String outTradeNo;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private int timestamp;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
