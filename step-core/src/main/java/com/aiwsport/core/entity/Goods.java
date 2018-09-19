package com.aiwsport.core.entity;

public class Goods {
    private Integer id;

    private String goodName;

    private String descInfo;

    private Integer count;

    private String type;

    private Integer salecoin;

    private String imagesurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getDescInfo() {
        return descInfo;
    }

    public void setDescInfo(String descInfo) {
        this.descInfo = descInfo;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}