package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 * 比较图片信息
 */
public class MatchCompare {

    private String res_code;

    private double similarity;

    public String getRes_code() {
        return res_code;
    }

    public void setRes_code(String res_code) {
        this.res_code = res_code;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return "MatchCompare{" +
                "res_code='" + res_code + '\'' +
                ", similarity=" + similarity +
                '}';
    }
}
