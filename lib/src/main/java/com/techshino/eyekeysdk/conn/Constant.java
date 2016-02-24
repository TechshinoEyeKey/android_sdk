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
    String APP_ID = "8e322a50992e4907a1230b14f3389164";
    String APP_KEY = "867ded663b1443cdb8195ca0af14cbb4";
    String APP_ID_KEY = "app_id=" + APP_ID + "&app_key=" + APP_KEY;


    /**
     * 用户不存在
     */
    String RES_CODE_1025 = "1025";

    String RES_CODE_0000 = "0000";

}
