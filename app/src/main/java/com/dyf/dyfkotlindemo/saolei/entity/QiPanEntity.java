package com.dyf.dyfkotlindemo.saolei.entity;


import java.util.Random;

/**封装棋盘*/
public class QiPanEntity {

    ItemEntity[][] qipan;
    private int level;
    private int leiCount;
    /**边界格*/
    private ItemEntity bianJie = new ItemEntity(false,0,false,true);

    public QiPanEntity(int level){
        this.level = level;
        qipan = new ItemEntity[level+2][level+2];
//        leiCount = level*level/5;
        leiCount = level;
        initLei();
    }

    public void initLei(){
        //初始化棋盘
        for(int i = 0;i<level+2;i++){
            for(int j = 0;j<level+2;j++){
                if(i == 0 || j == 0 || i==level+1 || j==level+1){
                    qipan[i][j] = bianJie;
                }else{
                    qipan[i][j] = new ItemEntity();
                }
            }
        }
        Random rand = new Random(System.currentTimeMillis());
        //分配雷
        for(int x = 0;x<leiCount;x++){
            int i = rand.nextInt(level)+1;
            int j = rand.nextInt(level)+1;
            if(qipan[i][j].isLei()){
                x--;
                continue;
            }else{
                qipan[i][j].setLei(true);
            }
        }
        //初始化雷数
        for(int i = 1;i<=level;i++){
            for(int j = 1;j<=level;j++){
                setLeiCount(qipan[i][j],i,j);
            }
        }
        syso();  //生成棋盘后打印整个棋盘，帮助进行测试
    }

    /**为单元格计算雷的个数*/
    private void setLeiCount(ItemEntity entity,int i,int j){
        int count = 0;
        for(int ii = i-1;ii<=i+1;ii++){
            for(int jj = j-1;jj<=j+1;jj++){
                if(qipan[ii][jj].isLei())
                    count++;
            }
        }
        entity.setLeiCount(count);
    }

    /**获取某个位置的item*/
    public ItemEntity getEntity(int position){
        ItemEntity entity = null;
        entity = qipan[(position)/level+1][position%level+1];
        return entity;
    }

    /**判断游戏是否成功*/
    public boolean isWin(){
        int count = 0;
        for(int i = 1;i<=level;i++){
            for(int j = 1;j<=level;j++){
                if(!qipan[i][j].isShow()){
                    count++;
                }
            }
        }
        return count == leiCount;  //未开启单元格与雷的个数一致，证明游戏胜利
    }

    /**测试方法：打印整个棋盘*/
    private void syso() {
        for(int i = 1;i<=level;i++){
            for(int j = 1;j<=level;j++){
                if(qipan[i][j].isLei()){
                    System.out.print("*");
                }else if(qipan[i][j].getLeiCount() == 0){
                    System.out.print("-");
                }else{
                    System.out.print(qipan[i][j].getLeiCount());
                }
            }
            System.out.println();
        }
    }

    /**显示所有雷的方法*/
    public void showLei(){
        for(int i = 1;i<=level;i++){
            for(int j =1;j<=level;j++){
                if(qipan[i][j].isLei() && !qipan[i][j].isShow()){
                    qipan[i][j].setShow(true);
                    qipan[i][j].setAutoShow(true);
                }
            }
        }
    }

    /**显示无害区域*/
    public void showNoLei(int position){
        if(position>=level*level || position<0){
            return;
        }
        int i = position/level+1;
        int j = position%level+1;
        if(qipan[i][j].isBian()){
            return;
        }
        if(qipan[i][j].getLeiCount() != 0 || qipan[i][j].isShow()){
            qipan[i][j].setShow(true);
            return;
        }
        qipan[i][j].setShow(true);
        for(int ii = i-1;ii<=i+1;ii++){
            for(int jj = j-1;jj<=j+1;jj++){
                if(ii<=0 || jj<=0 || ii>=level+1 || jj>=level+1){
                    continue;
                }
                showNoLei((ii-1)*level+(jj-1));
            }
        }
    }

    /**修改标记状态（如果标记的不是雷，则修改状态，使其显示错误雷的图片）*/
    public void showBiaoJi(){
        for(int i = 1;i<=level;i++){
            for(int j = 1;j<=level;j++){
                if(qipan[i][j].isBiaoJi() && qipan[i][j].isLei()){
                    qipan[i][j].setBiaoJiWrong(false);
                }else if(qipan[i][j].isBiaoJi() && !qipan[i][j].isLei()){
                    qipan[i][j].setBiaoJiWrong(true);
                }
            }
        }
    }
}
