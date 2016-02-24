package com.techshino.eyekeydemo.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class FaceGatherCreate {

    private String res_code;
    private String message;
    private int face_count;
    private String facegather_id;
    private String facegather_name;
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

    public int getFace_count() {
        return face_count;
    }

    public void setFace_count(int face_count) {
        this.face_count = face_count;
    }

    public String getFacegather_id() {
        return facegather_id;
    }

    public void setFacegather_id(String facegather_id) {
        this.facegather_id = facegather_id;
    }

    public String getFacegather_name() {
        return facegather_name;
    }

    public void setFacegather_name(String facegather_name) {
        this.facegather_name = facegather_name;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return "FaceGatherCreate{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", face_count=" + face_count +
                ", facegather_id='" + facegather_id + '\'' +
                ", facegather_name='" + facegather_name + '\'' +
                ", tip='" + tip + '\'' +
                '}';
    }
}
