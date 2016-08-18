package com.techshino.eyekeydev.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class PeopleSet {

  String res_code;

  String people_id;

  String people_name;

  String message;

  public String getRes_code() {
    return res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  public String getPeople_id() {
    return people_id;
  }

  public void setPeople_id(String people_id) {
    this.people_id = people_id;
  }

  public String getPeople_name() {
    return people_name;
  }

  public void setPeople_name(String people_name) {
    this.people_name = people_name;
  }

  @Override
  public String toString() {
    return "PeopleSet{" +
        "res_code='" + res_code + '\'' +
        ", people_id='" + people_id + '\'' +
        ", people_name='" + people_name + '\'' +
        ", message='" + message + '\'' +
        '}';
  }
}
