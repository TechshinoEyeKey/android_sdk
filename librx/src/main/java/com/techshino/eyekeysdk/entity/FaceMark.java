package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 * 人脸49点
 */
public class FaceMark {
    private String face_id;

    private Mark mark;

    private String res_code;

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getFace_id() {
        return this.face_id;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public Mark getMark() {
        return this.mark;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_code() {
        return this.res_code;
    }

    @Override
    public String toString() {
        return "FaceMark{" +
                "face_id='" + face_id + '\'' +
                ", mark=" + mark +
                ", res_code='" + res_code + '\'' +
                '}';
    }
}
