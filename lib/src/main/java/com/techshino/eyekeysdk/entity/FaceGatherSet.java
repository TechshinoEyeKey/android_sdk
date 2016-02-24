package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class FaceGatherSet {

    private String res_code;
    private String message;
    private String facegather_name;
    private String facegather_id;
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

    public String getFacegather_name() {
        return facegather_name;
    }

    public void setFacegather_name(String facegather_name) {
        this.facegather_name = facegather_name;
    }

    public String getFacegather_id() {
        return facegather_id;
    }

    public void setFacegather_id(String facegather_id) {
        this.facegather_id = facegather_id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "FaceGatherSet{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", facegather_name='" + facegather_name + '\'' +
                ", facegather_id='" + facegather_id + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}
