package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class CrowdAddAndRemove {

    private String res_code;

    private String message;

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
        return "CrowdAddAndRemove{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", success=" + success +
                '}';
    }
}
