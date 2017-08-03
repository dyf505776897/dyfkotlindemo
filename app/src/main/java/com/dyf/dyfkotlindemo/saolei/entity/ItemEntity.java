package com.dyf.dyfkotlindemo.saolei.entity;

/**
 * Created by dyf on 2017/8/3.
 */


/**封装扫雷的每一个格子*/
public class ItemEntity {

    /**是否显示当前格*/
    private boolean isShow;

    /**当前格的雷数*/
    private int leiCount;

    /**当前格是否为雷*/
    private boolean isLei;

    /**当前格是否为边界棋盘*/
    private boolean isBian;

    /**是否为自动显示的雷*/
    private boolean isAutoShow = false;

    /**是否被标记*/
    private boolean isBiaoJi;
    /**雷的标记是错误的*/
    private boolean isBiaoJiWrong;

    public boolean isBiaoJiWrong() {
        return isBiaoJiWrong;
    }

    public void setBiaoJiWrong(boolean isBiaoJiWrong) {
        this.isBiaoJiWrong = isBiaoJiWrong;
    }

    public boolean isBiaoJi(){
        return isBiaoJi;
    }

    public void setBiaoJi(boolean isBiaoJi){
        this.isBiaoJi = isBiaoJi;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public int getLeiCount() {
        return leiCount;
    }

    public void setLeiCount(int leiCount) {
        this.leiCount = leiCount;
    }

    public boolean isLei() {
        return isLei;
    }

    public void setLei(boolean isLei) {
        this.isLei = isLei;
    }

    public boolean isBian() {
        return isBian;
    }

    public void setBian(boolean isBian) {
        this.isBian = isBian;
    }

    public ItemEntity(boolean isShow, int leiCount, boolean isLei,
                      boolean isBian) {
        super();
        this.isShow = isShow;
        this.leiCount = leiCount;
        this.isLei = isLei;
        this.isBian = isBian;
    }

    public ItemEntity(){

    }

    public void setAutoShow(boolean isAutoShow){
        this.isAutoShow = isAutoShow;
    }

    public boolean isAutoShow(){
        return isAutoShow;
    }

}