package com.dyf.dyfkotlindemo.saolei.entity;

/**封装用户分数数据*/
public class UserScoreEntity {

    /**用户姓名*/
    private String name;
    /**用户分数*/
    private int score;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public UserScoreEntity(String name, int score) {
        super();
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "姓名： "+name+",成绩："+score;
    }
}
