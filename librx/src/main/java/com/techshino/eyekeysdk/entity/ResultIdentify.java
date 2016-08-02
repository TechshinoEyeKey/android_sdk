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

  public String getPerson_id() {
    return person_id;
  }

  public void setPerson_id(String person_id) {
    this.person_id = person_id;
  }

  public String getPerson_name() {
    return person_name;
  }

  public void setPerson_name(String person_name) {
    this.person_name = person_name;
  }
}
