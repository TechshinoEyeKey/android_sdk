package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class People {

    private String people_name;

    private String people_id;

    private String tip;

    public String getPeople_name() {
        return people_name;
    }

    public void setPeople_name(String people_name) {
        this.people_name = people_name;
    }

    public String getPeople_id() {
        return people_id;
    }

    public void setPeople_id(String people_id) {
        this.people_id = people_id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "People{" +
                "people_name='" + people_name + '\'' +
                ", people_id='" + people_id + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}
