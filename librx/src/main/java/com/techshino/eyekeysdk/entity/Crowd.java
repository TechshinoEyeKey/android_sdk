package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/3/23.
 */
public class Crowd {

  String crowd_id;
  String crowd_name;

  public String getCrowd_id() {
    return crowd_id;
  }

  public void setCrowd_id(String crowd_id) {
    this.crowd_id = crowd_id;
  }

  public String getCrowd_name() {
    return crowd_name;
  }

  public void setCrowd_name(String crowd_name) {
    this.crowd_name = crowd_name;
  }

  @Override
  public String toString() {
    return "Crowd{" +
        "crowd_id='" + crowd_id + '\'' +
        ", crowd_name='" + crowd_name + '\'' +
        '}';
  }
}
