package com.techshino.eyekeysdk.entity;

/**
 * Created by wangzhi on 2016/1/22.
 */
public class AppInfo {

  private String res_code;

  private String message;

  private String name;

  private String description;

  public String getRes_code() {
    return res_code;
  }

  public void setRes_code(String res_code) {
    this.res_code = res_code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "AppInfo{" +
        "res_code='" + res_code + '\'' +
        ", message='" + message + '\'' +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
