package com.dyf.dyfkotlindemo.model;

import java.io.Serializable;

/**
 * Created by dyf on 2017/7/13.
 */

public class Goods implements Serializable {

    /**
     * gid : 18
     * gname : Air Jordan 12 Low AJ12 狼灰 麂皮 308317 308305-002
     * attr_name : 黄色|29
     * num : 1
     * price : 0.01
     * total : 0.01
     */

    private String gid;
    private String gname;
    private String attr_name;
    private String num;
    private String price;
    private String total;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
