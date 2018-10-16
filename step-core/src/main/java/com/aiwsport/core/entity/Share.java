package com.aiwsport.core.entity;

import java.util.Date;

public class Share {
    private Integer id;

    private Integer muserid;

    private Integer suserid;

    private String isaddreward;

    private String createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMuserid() {
        return muserid;
    }

    public void setMuserid(Integer muserid) {
        this.muserid = muserid;
    }

    public Integer getSuserid() {
        return suserid;
    }

    public void setSuserid(Integer suserid) {
        this.suserid = suserid;
    }

    public String getIsaddreward() {
        return isaddreward;
    }

    public void setIsaddreward(String isaddreward) {
        this.isaddreward = isaddreward;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}