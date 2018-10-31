package com.aiwsport.core.entity;

import java.util.List;

/**
 * Created by yangjian9 on 2018/9/29.
 */
public class QueryActivestepShow {
    private Integer id;

    private Integer userid;

    private Integer sumstep;

    private String nickname;

    private String avatarurl;

    private String showtitle;

    private List<User> zanUser;

    private Integer index;

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

    public List<User> getZanUser() {
        return zanUser;
    }

    public void setZanUser(List<User> zanUser) {
        this.zanUser = zanUser;
    }

    public String getShowtitle() {
        return showtitle;
    }

    public void setShowtitle(String showtitle) {
        this.showtitle = showtitle;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
