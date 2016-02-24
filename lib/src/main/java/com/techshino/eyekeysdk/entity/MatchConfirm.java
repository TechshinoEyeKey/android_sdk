package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class MatchConfirm {

    boolean result;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MatchConfirm{" +
                "result=" + result +
                '}';
    }
}
