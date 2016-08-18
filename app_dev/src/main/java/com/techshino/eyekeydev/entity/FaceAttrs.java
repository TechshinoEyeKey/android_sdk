package com.techshino.eyekeydev.entity;

import java.util.List;

/**
 * 服务器返回人脸参数
 *
 * @author wangzhi
 */
public class FaceAttrs {
  private List<Face> face;

  private int img_height;

  private String img_id;

  private int img_width;

  private String res_code;

  public List<Face> getFace() {
    return this.face;
  }

  public void setFace(List<Face> face) {
    this.face = face;
  }

  public int getImg_height() {
    return this.img_height;
  }

  public void setImg_height(int img_height) {
    this.img_height = img_height;
  }

  public String getImg_id() {
    return this.img_id;
  }

  public void setImg_id(String img_id) {
    this.img_id = img_id;
  }

  public int getImg_width() {
    return this.img_width;
  }

  public void setImg_width(int img_width) {
    this.img_width = img_width;
  }

  public String getRes_code() {
    return this.res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  @Override
  public String toString() {
    return "FaceAttrs [face=" + face + ", img_height=" + img_height
        + ", img_id=" + img_id + ", img_width=" + img_width
        + ", res_code=" + res_code + "]";
  }
}
