package com.techshino.eyekeydev.entity;

import java.util.List;

/**
 * 根据用户名获取人脸信息
 * <p>
 * Created by wangzhi on 2015/12/9.
 */
public class PeopleGet {

  private List<String> crowd;

  private int crowd_count;

  private int face_count;

  private List<String> face_id;

  private int fingerprint_count;

  private List<String> fingerprint_id;

  private int iris_count;

  private List<String> iris_id;

  private String people_id;

  private String people_name;

  private String res_code;

  public List<String> getCrowd() {
    return crowd;
  }

  public void setCrowd(List<String> crowd) {
    this.crowd = crowd;
  }

  public int getCrowd_count() {
    return crowd_count;
  }

  public void setCrowd_count(int crowd_count) {
    this.crowd_count = crowd_count;
  }

  public int getFace_count() {
    return face_count;
  }

  public void setFace_count(int face_count) {
    this.face_count = face_count;
  }

  public List<String> getFace_id() {
    return face_id;
  }

  public void setFace_id(List<String> face_id) {
    this.face_id = face_id;
  }

  public int getFingerprint_count() {
    return fingerprint_count;
  }

  public void setFingerprint_count(int fingerprint_count) {
    this.fingerprint_count = fingerprint_count;
  }

  public List<String> getFingerprint_id() {
    return fingerprint_id;
  }

  public void setFingerprint_id(List<String> fingerprint_id) {
    this.fingerprint_id = fingerprint_id;
  }

  public int getIris_count() {
    return iris_count;
  }

  public void setIris_count(int iris_count) {
    this.iris_count = iris_count;
  }

  public List<String> getIris_id() {
    return iris_id;
  }

  public void setIris_id(List<String> iris_id) {
    this.iris_id = iris_id;
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

  public String getRes_code() {
    return res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  @Override
  public String toString() {
    return "PeopleGet{" +
        "crowd=" + crowd +
        ", crowd_count=" + crowd_count +
        ", face_count=" + face_count +
        ", face_id=" + face_id +
        ", fingerprint_count=" + fingerprint_count +
        ", fingerprint_id=" + fingerprint_id +
        ", iris_count=" + iris_count +
        ", iris_id=" + iris_id +
        ", people_id='" + people_id + '\'' +
        ", people_name='" + people_name + '\'' +
        ", res_code='" + res_code + '\'' +
        '}';
  }
}
