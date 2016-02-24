package com.techshino.eyekeydemo.entity;

import java.util.List;

/**
 * Created by wangzhi on 2016/1/21.
 * /match_identify接口face实体
 */
public class FaceIdentify {

    private List<ResultIdentify> result;

    private String face_id;

    private String img_id;

    private String url;

    public List<ResultIdentify> getResult() {
        return result;
    }

    public void setResult(List<ResultIdentify> result) {
        this.result = result;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
