package com.techshino.eyekeydemo.entity;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class CrowdDelete {

    private String res_code;

    private String message;

    private String crowd_name;

    private int count;

    private boolean success;

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

    public String getCrowd_name() {
        return crowd_name;
    }

    public void setCrowd_name(String crowd_name) {
        this.crowd_name = crowd_name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CrowdDelete{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", crowd_name='" + crowd_name + '\'' +
                ", count=" + count +
                ", success=" + success +
                '}';
    }
}
