package com.techshino.eyekeydev.entity;

import java.util.List;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class CrowdGet {

  private String res_code;

  private String message;

  private int people_count;

  private String crowd_name;

  private String crowd_id;

  private String tip;

  private List<People> people;

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

  public int getPeople_count() {
    return people_count;
  }

  public void setPeople_count(int people_count) {
    this.people_count = people_count;
  }

  public String getCrowd_name() {
    return crowd_name;
  }

  public void setCrowd_name(String crowd_name) {
    this.crowd_name = crowd_name;
  }

  public String getCrowd_id() {
    return crowd_id;
  }

  public void setCrowd_id(String crowd_id) {
    this.crowd_id = crowd_id;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public List<People> getPeople() {
    return people;
  }

  public void setPeople(List<People> people) {
    this.people = people;
  }

  @Override
  public String toString() {
    return "CrowdGet{" +
        "res_code='" + res_code + '\'' +
        ", message='" + message + '\'' +
        ", people_count=" + people_count +
        ", crowd_name='" + crowd_name + '\'' +
        ", crowd_id='" + crowd_id + '\'' +
        ", tip='" + tip + '\'' +
        ", people=" + people +
        '}';
  }
}
