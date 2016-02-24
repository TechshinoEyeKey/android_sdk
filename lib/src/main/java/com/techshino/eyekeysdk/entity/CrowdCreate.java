package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class CrowdCreate {

    private String res_code;

    private String message;

    private int added_people;

    private String crowd_name;

    private String crowd_id;

    private String tip;

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAdded_people() {
        return added_people;
    }

    public void setAdded_people(int added_people) {
        this.added_people = added_people;
    }

    public String getCrowd_name() {
        return crowd_name;
    }

    public void setCrowd_name(String crowd_name) {
        this.crowd_name = crowd_name;
    }

    public String getCrowd_id() {
        return crowd_id;
    }

    public void setCrowd_id(String crowd_id) {
        this.crowd_id = crowd_id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "CrowdCreate{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", added_people=" + added_people +
                ", crowd_name='" + crowd_name + '\'' +
                ", crowd_id='" + crowd_id + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}
