package com.techshino.eyekeysdk.conn;

public interface Constant {
  /**
   * 访问云平台服务接口的地址
   */
  String API_SERVER = "http://api.eyekey.com/";
  String Check = API_SERVER + "/face/Check";
  String Match = API_SERVER + "/face/Match";
  String FaceGather = API_SERVER + "/FaceGather";
  String People = API_SERVER + "/People";
  String Crowd = API_SERVER + "/Crowd";
  String Info = API_SERVER + "/Info";
  String APP_ID = "4df3b721cb8949c2a0e629b1b9233f8e";
  String APP_KEY = "543b3f73e66f4efb8332ee367b7baaa0";
  /**
   * 用户不存在
   */
  String RES_CODE_1025 = "1025";

  String RES_CODE_0000 = "0000";

}
