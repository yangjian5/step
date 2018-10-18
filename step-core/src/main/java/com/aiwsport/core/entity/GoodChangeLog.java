package com.aiwsport.core.entity;

import java.util.Date;

public class GoodChangeLog {
    private Integer id;

    private Integer userid;

    private Integer goodid;

    private String createtime;

    private String status;

    private String kdnum;

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

    public Integer getGoodid() {
        return goodid;
    }

    public void setGoodid(Integer goodid) {
        this.goodid = goodid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKdnum() {
        return kdnum;
    }

    public void setKdnum(String kdnum) {
        this.kdnum = kdnum;
    }
}