package com.dyf.dyfkotlindemo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dyf on 2017/7/13.
 */

public class Info implements Serializable {

    /**
     * order_id : 64
     * price : 0.01
     * order_sn : 201707111809105964a3c6c9239
     * time : 2017-07-11 18:09:10
     * goods_amount : 0.01
     * fee : 0.00
     * num : 1
     * status : 0
     * pay_status : 1
     * goodslist : [{"gid":"18","gname":"Air Jordan 12 Low AJ12 狼灰 麂皮 308317 308305-002","attr_name":"黄色|29","num":"1","price":"0.01","total":"0.01"}]
     */

    private String order_id;
    private String price;
    private String order_sn;
    private String time;
    private String goods_amount;
    private String fee;
    private String num;
    private String status;
    private String pay_status;
    private List<Goods> goodslist;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public List<Goods> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<Goods> goodslist) {
        this.goodslist = goodslist;
    }
}
