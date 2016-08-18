package com.techshino.eyekeydev.entity;

/**
 * 比对信息实体
 * <p>
 * Created by wangzhi on 2015/12/8.
 */
public class MatchVerify {

  private String message;

  private String res_code;

  private boolean result;

  private double similarity;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getRes_code() {
    return res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public double getSimilarity() {
    return similarity;
  }

  public void setSimilarity(double similarity) {
    this.similarity = similarity;
  }

  @Override
  public String toString() {
    return "MatchVerify{" +
        "message='" + message + '\'' +
        ", res_code='" + res_code + '\'' +
        ", result=" + result +
        ", similarity=" + similarity +
        '}';
  }
}
