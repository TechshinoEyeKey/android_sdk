package com.techshino.eyekeysdk.entity;

import java.util.List;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class MatchSearch {

    private String res_code;
    private String message;
    private List<Result> result;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MatchSearch{" +
                "res_code='" + res_code + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
