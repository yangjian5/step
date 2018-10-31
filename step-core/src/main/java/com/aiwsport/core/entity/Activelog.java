package com.aiwsport.core.entity;

import java.util.Date;

public class Activelog {
    private Integer id;

    private Integer userid;

    private Integer activestepid;

    private String type;

    private Double coin;

    private String createtime;

    private String isfinish;

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

    public Integer getActivestepid() {
        return activestepid;
    }

    public void setActivestepid(Integer activestepid) {
        this.activestepid = activestepid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCoin() {
        return coin;
    }

    public void setCoin(Double coin) {
        this.coin = coin;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getIsfinish() {
        return isfinish;
    }

    public void setIsfinish(String isfinish) {
        this.isfinish = isfinish;
    }
}