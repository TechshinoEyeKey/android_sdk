package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/21.
 * /match_identify接口resullt实体
 */
public class ResultIdentify {

  private double similarity;

  private String person_id;

  private String person_name;

  public double getSimilarity() {
    return this.similarity;
  }

  public void setSimilarity(double similarity) {
    this.similarity = similarity;
  }

  @Override
  public String toString() {
    return "ResultIdentify{" +
        "similarity=" + similarity +
        ", person_id='" + person_id + '\'' +
        ", person_name='" + person_name + '\'' +
        '}';
  }
}
