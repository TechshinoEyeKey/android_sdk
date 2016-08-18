package com.techshino.eyekeydev.entity;

/**
 * 人脸参数
 *
 * @author wangzhi
 */
public class Attribute {
  private int age;// 包含年龄分析结果，值为一个非负整数表示估计的年龄

  private String gender;// 包含性别分析结果，值为Male/Female

  private int lefteye_opendegree;// 检出人脸左眼的睁开程度(0~100之间的整数)

  private int mouth_opendegree;// 检出人脸的嘴巴张开程度(0~100之间的整数)

  private Pose pose;// 包含脸部姿势分析结果，歪头tilting，抬低头raise，摇头turn。每个值为[-90,90]近似角度值；平面内左右歪头：正左、负右；扭头：正左、负右；低抬头：正抬、负低;

  private int righteye_opendegree;// 检出人脸右眼的睁开程度(0~100之间的整数)

  public int getAge() {
    return this.age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getGender() {
    return this.gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public int getLefteye_opendegree() {
    return this.lefteye_opendegree;
  }

  public void setLefteye_opendegree(int lefteye_opendegree) {
    this.lefteye_opendegree = lefteye_opendegree;
  }

  public int getMouth_opendegree() {
    return this.mouth_opendegree;
  }

  public void setMouth_opendegree(int mouth_opendegree) {
    this.mouth_opendegree = mouth_opendegree;
  }

  public Pose getPose() {
    return this.pose;
  }

  public void setPose(Pose pose) {
    this.pose = pose;
  }

  public int getRighteye_opendegree() {
    return this.righteye_opendegree;
  }

  public void setRighteye_opendegree(int righteye_opendegree) {
    this.righteye_opendegree = righteye_opendegree;
  }

  @Override
  public String toString() {
    return "Attribute [age=" + age + ", gender=" + gender
        + ", lefteye_opendegree=" + lefteye_opendegree
        + ", mouth_opendegree=" + mouth_opendegree + ", pose=" + pose
        + ", righteye_opendegree=" + righteye_opendegree + "]";
  }

}
