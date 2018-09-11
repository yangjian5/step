package com.aiwsport.core.entity;

public class Goods {
    private Integer id;

    private String name;

    private String desc;

    private Integer count;

    private Integer salecoin;

    private String imagesurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSalecoin() {
        return salecoin;
    }

    public void setSalecoin(Integer salecoin) {
        this.salecoin = salecoin;
    }

    public String getImagesurl() {
        return imagesurl;
    }

    public void setImagesurl(String imagesurl) {
        this.imagesurl = imagesurl;
    }
}