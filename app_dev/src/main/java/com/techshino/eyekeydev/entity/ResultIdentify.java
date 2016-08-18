package com.techshino.eyekeydev.entity;

/**
 * Created by wangzhi on 2016/1/21.
 * /match_identify接口resullt实体
 */
public class ResultIdentify {

  private double similarity;

  private String people_id;

  private String people_name;

  public double getSimilarity() {
    return this.similarity;
  }

  public void setSimilarity(double similarity) {
    this.similarity = similarity;
  }

  public String getPeople_id() {
    return this.people_id;
  }

  public void setPeople_id(String people_id) {
    this.people_id = people_id;
  }

  public String getPeople_name() {
    return this.people_name;
  }

  public void setPeople_name(String people_name) {
    this.people_name = people_name;
  }
}
