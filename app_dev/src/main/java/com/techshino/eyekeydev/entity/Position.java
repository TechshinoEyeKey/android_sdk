package com.techshino.eyekeydev.entity;

/**
 * 位置信息
 *
 * @author wangzhi
 */
public class Position {
  private Center center;

  private EyeLeft eye_left;

  private EyeRight eye_right;

  private int height;

  private int width;

  public Center getCenter() {
    return this.center;
  }

  public void setCenter(Center center) {
    this.center = center;
  }

  public EyeLeft getEye_left() {
    return eye_left;
  }

  public void setEye_left(EyeLeft eye_left) {
    this.eye_left = eye_left;
  }

  public EyeRight getEye_right() {
    return eye_right;
  }

  public void setEye_right(EyeRight eye_right) {
    this.eye_right = eye_right;
  }

  public int getHeight() {
    return this.height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return this.width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public String toString() {
    return "Position [center=" + center + ", eye_left=" + eye_left
        + ", eye_right=" + eye_right + ", height=" + height
        + ", width=" + width + "]";
  }

}
