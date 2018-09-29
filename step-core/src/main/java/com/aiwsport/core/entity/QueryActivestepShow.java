package com.aiwsport.core.entity;

/**
 * Created by yangjian9 on 2018/9/29.
 */
public class QueryActivestepShow {
    private Integer id;

    private Integer userid;

    private Integer sumstep;

    private String nickname;

    private String avatarurl;

    private Integer zanuserid;

    private String zanavatarurl;

    private String zannickname;

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

    public Integer getSumstep() {
        return sumstep;
    }

    public void setSumstep(Integer sumstep) {
        this.sumstep = sumstep;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public Integer getZanuserid() {
        return zanuserid;
    }

    public void setZanuserid(Integer zanuserid) {
        this.zanuserid = zanuserid;
    }

    public String getZanavatarurl() {
        return zanavatarurl;
    }

    public void setZanavatarurl(String zanavatarurl) {
        this.zanavatarurl = zanavatarurl;
    }

    public String getZannickname() {
        return zannickname;
    }

    public void setZannickname(String zannickname) {
        this.zannickname = zannickname;
    }
}
