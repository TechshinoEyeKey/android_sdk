package com.techshino.eyekeydev.entity;

/**
 * Created by wangzhi on 2016/1/21.
 */
public class Result {

  private String face_id;

  private int similarity;

  private String img_id;

  private String people_name;

  public String getFace_id() {
    return this.face_id;
  }

  public void setFace_id(String face_id) {
    this.face_id = face_id;
  }

  public int getSimilarity() {
    return this.similarity;
  }

  public void setSimilarity(int similarity) {
    this.similarity = similarity;
  }

  public String getImg_id() {
    return this.img_id;
  }

  public void setImg_id(String img_id) {
    this.img_id = img_id;
  }

  public String getPeople_name() {
    return this.people_name;
  }

  public void setPeople_name(String people_name) {
    this.people_name = people_name;
  }
}
