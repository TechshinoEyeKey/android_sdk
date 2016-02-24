package com.techshino.eyekeysdk.entity;

import java.util.List;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class MatchIdentify {

    private String res_code;
    private String message;
    private List<FaceIdentify> face;

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

    public List<FaceIdentify> getFace() {
        return face;
    }

    public void setFace(List<FaceIdentify> face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "MatchIdentify{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", face=" + face +
                '}';
    }
}
