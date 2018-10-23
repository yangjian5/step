package com.aiwsport.core.entity;

import java.util.Date;

/**
 * Created by yangjian9 on 2018/10/23.
 */
public class QueryCommentShow {

    private Integer commentid;

    private Integer userid;

    private Integer activestepid;

    private String content;

    private String createtime;

    private double coinnum;

    private String nickname;

    private String avatarurl;

    private String gender;

    private String city;

    private String province;

    private String country;

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
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

    public double getCoinnum() {
        return coinnum;
    }

    public void setCoinnum(double coinnum) {
        this.coinnum = coinnum;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
