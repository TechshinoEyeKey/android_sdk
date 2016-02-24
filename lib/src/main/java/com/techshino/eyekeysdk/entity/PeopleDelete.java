package com.techshino.eyekeysdk.entity;

/**
 * 删除用户返回信息
 * <p>
 * Created by wangzhi on 2015/12/9.
 */
public class PeopleDelete {

    private int deleted;

    private String message;

    private String res_code;

    private boolean success;

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getDeleted() {
        return this.deleted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public String getRes_code() {
        return this.res_code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    @Override
    public String toString() {
        return "PeopleDelete{" +
                "deleted=" + deleted +
                ", message='" + message + '\'' +
                ", res_code='" + res_code + '\'' +
                ", success=" + success +
                '}';
    }
}
