package com.aiwsport.core.entity;


public class StepChangeLog {
    private Integer id;

    private Integer userid;

    private Integer stepnum;

    private Integer jiachenstep;

    private Integer rewardstep;

    private double coinnum;

    private String createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getStepnum() {
        return stepnum;
    }

    public void setStepnum(Integer stepnum) {
        this.stepnum = stepnum;
    }

    public double getCoinnum() {
        return coinnum;
    }

    public void setCoinnum(double coinnum) {
        this.coinnum = coinnum;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getJiachenstep() {
        return jiachenstep;
    }

    public void setJiachenstep(Integer jiachenstep) {
        this.jiachenstep = jiachenstep;
    }

    public Integer getRewardstep() {
        return rewardstep;
    }

    public void setRewardstep(Integer rewardstep) {
        this.rewardstep = rewardstep;
    }
}