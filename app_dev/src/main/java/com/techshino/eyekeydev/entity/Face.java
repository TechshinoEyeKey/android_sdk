package com.techshino.eyekeydev.entity;

/**
 * 人脸信息
 *
 * @author wangzhi
 */
public class Face {

  private Attribute attribute;

  private String face_id;

  private Position position;

  private String tag;

  public Attribute getAttribute() {
    return this.attribute;
  }

  public void setAttribute(Attribute attribute) {
    this.attribute = attribute;
  }

  public String getFace_id() {
    return this.face_id;
  }

  public void setFace_id(String face_id) {
    this.face_id = face_id;
  }

  public Position getPosition() {
    return this.position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public String getTag() {
    return this.tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return "Face [attribute=" + attribute + ", face_id=" + face_id
        + ", position=" + position + ", tag=" + tag + "]";
  }
}
