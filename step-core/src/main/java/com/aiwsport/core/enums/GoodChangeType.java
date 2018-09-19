package com.aiwsport.core.enums;

public enum GoodChangeType {
    sended("发货", 1), notSend("未发货", 2), finish("已完成", 3);


    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private GoodChangeType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.index + "_" + this.name;
    }

    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

}
