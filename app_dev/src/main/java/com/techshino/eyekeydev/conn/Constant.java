package com.techshino.eyekeydev.conn;

public interface Constant {
  /**
   * 访问云平台服务接口的地址
   */
  String API_SERVER = "http://api.eyekey.com/";
  String Check = API_SERVER + "/face/Check";
  String Match = API_SERVER + "/face/Match";
  String FaceGather = API_SERVER + "/face/FaceGather";
  String People = API_SERVER + "/People";
  String Crowd = API_SERVER + "/Crowd";
  String Info = API_SERVER + "/Info";
  String APP_ID = "ea79bd9e1046417db25749d564d5c484";
  String APP_KEY = "5f6ab67f12ed4b159c3037b6c8421255";

  /**
   * 用户不存在
   */
  String RES_CODE_1025 = "1025";

  String RES_CODE_0000 = "0000";

}
