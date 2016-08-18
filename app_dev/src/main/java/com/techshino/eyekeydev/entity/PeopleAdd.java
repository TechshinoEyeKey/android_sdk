package com.techshino.eyekeydev.entity;

/**
 * 给people添加faceId
 * <p>
 * Created by wangzhi on 2015/12/28.
 */
public class PeopleAdd {
  private int face_added;

  private int fingerprint_added;

  private int iris_added;

  private String message;

  private String res_code;

  private boolean success;

  public int getFace_added() {
    return this.face_added;
  }

  public void setFace_added(int face_added) {
    this.face_added = face_added;
  }

  public int getFingerprint_added() {
    return this.fingerprint_added;
  }

  public void setFingerprint_added(int fingerprint_added) {
    this.fingerprint_added = fingerprint_added;
  }

  public int getIris_added() {
    return this.iris_added;
  }

  public void setIris_added(int iris_added) {
    this.iris_added = iris_added;
  }

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getRes_code() {
    return this.res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  public boolean getSuccess() {
    return this.success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  @Override
  public String toString() {
    return "PeopleAdd{" +
        "face_added=" + face_added +
        ", fingerprint_added=" + fingerprint_added +
        ", iris_added=" + iris_added +
        ", message='" + message + '\'' +
        ", res_code='" + res_code + '\'' +
        ", success=" + success +
        '}';
  }
}
