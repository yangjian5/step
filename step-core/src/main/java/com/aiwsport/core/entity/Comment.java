package com.aiwsport.core.entity;

import java.util.Date;

public class Comment {
    private Integer id;

    private Integer userid;

    private Integer activestepid;

    private String content;

    private String createtime;

    private String status;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}