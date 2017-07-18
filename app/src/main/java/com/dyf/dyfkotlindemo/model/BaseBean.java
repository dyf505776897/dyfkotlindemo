package com.dyf.dyfkotlindemo.model;

/**
 * Created by dyf on 2017/7/13.
 */

public class BaseBean {


    /**
     * code : 200
     * message : 数据获取成功
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
