package com.techshino.eyekeysdk.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.techshino.eyekeysdk.conn.Constant;
import com.techshino.eyekeysdk.entity.AppInfo;
import com.techshino.eyekeysdk.entity.CrowdAddAndRemove;
import com.techshino.eyekeysdk.entity.CrowdCreate;
import com.techshino.eyekeysdk.entity.CrowdDelete;
import com.techshino.eyekeysdk.entity.CrowdGet;
import com.techshino.eyekeysdk.entity.FaceAttrs;
import com.techshino.eyekeysdk.entity.FaceGatherAddFace;
import com.techshino.eyekeysdk.entity.FaceGatherCreate;
import com.techshino.eyekeysdk.entity.FaceGatherDelete;
import com.techshino.eyekeysdk.entity.FaceGatherGet;
import com.techshino.eyekeysdk.entity.FaceGatherRemoveFace;
import com.techshino.eyekeysdk.entity.FaceGatherSet;
import com.techshino.eyekeysdk.entity.FaceMark;
import com.techshino.eyekeysdk.entity.MatchCompare;
import com.techshino.eyekeysdk.entity.MatchConfirm;
import com.techshino.eyekeysdk.entity.MatchIdentify;
import com.techshino.eyekeysdk.entity.MatchSearch;
import com.techshino.eyekeysdk.entity.MatchVerify;
import com.techshino.eyekeysdk.entity.PeopleAdd;
import com.techshino.eyekeysdk.entity.PeopleCreate;
import com.techshino.eyekeysdk.entity.PeopleDelete;
import com.techshino.eyekeysdk.entity.PeopleGet;
import com.techshino.eyekeysdk.entity.PeopleRemove;
import com.techshino.eyekeysdk.entity.PeopleSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wangzhi on 2016/1/15.
 * <p>
 * eyekey人脸识别API
 */
public class CheckAPI implements Constant {

  private static final String TAG = "CheckAPI";

  private static final String EYEKEY_APP_ID = "eyekey_appid";
  private static final String EYEKEY_APP_KEY = "eyekey_appkey";
  private static final Retrofit sRetrofit = new Retrofit.Builder()
      .baseUrl(API_SERVER)
      .addConverterFactory(GsonConverterFactory.create())
      .build();
  public static final EyekeyService sEyekeyManagerService = sRetrofit.create(EyekeyService.class);
  private static String sAppId = "";
  private static String sAppKey = "";
  private static ArrayList<Call> sCalls = new ArrayList<>();

  public static void init(Context context) {
    ApplicationInfo appInfo = null;
    try {
      appInfo = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(),
              PackageManager.GET_META_DATA);
      Bundle bundle = appInfo.metaData;
      if (bundle != null) {
        sAppId = bundle.getString(EYEKEY_APP_ID);
        sAppKey = bundle.getString(EYEKEY_APP_KEY);
      }
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    Log.i(TAG, "appid:" + sAppId + " appkey:" + sAppKey);
  }

  /**
   * 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性
   *
   * @param base64 待检测图片数据信息,通过POST方法上传的二进制数据，原始图片大小需要小于3M
   * @param mode   (可选)检测模式 (默认) oneface
   *               。在oneface模式中，检测器仅找出图片中最大的一张脸。如果图中有多张人脸大小相同，随机返回一张人脸信息。
   * @param tip    (可选)指定一个不包含^@,&=*'"等非法字符且不超过255字节的字符串作为tip
   */
  public static Call<FaceAttrs> checkingImageByBase64(String base64, String mode, String tip) {
    Call<FaceAttrs> call = sEyekeyManagerService.checkingImageBase64(sAppId, sAppKey, base64, mode, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性
   *
   * @param url  待检测图片url
   * @param mode (可选)检测模式 (默认) oneface
   *             。在oneface模式中，检测器仅找出图片中最大的一张脸。如果图中有多张人脸大小相同，随机返回一张人脸信息。
   * @param tip  (可选)指定一个不包含^@,&=*'"等非法字符且不超过255字节的字符串作为tip
   * @return
   */
  public static Call<FaceAttrs> checkingImageByUrl(String url, String mode, String tip) {
    Call<FaceAttrs> call = sEyekeyManagerService.checkingImageUrl(sAppId, sAppKey, url, mode, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 检测给定人脸(Face)相应的面部轮廓，五官等关键点的位置，包括49点一种模式。
   *
   * @param face_id 待检测人脸的face_id
   * @param type    (可选)表示返回的关键点个数，目前支持49p, 默认为49p
   * @return
   */
  public static Call<FaceMark> checkMark(String face_id, String type) {
    Call<FaceMark> call = sEyekeyManagerService.checkMark(sAppId, sAppKey, face_id, type);
    sCalls.add(call);
    return call;
  }

  /**
   * 计算两个Face的相似性以及五官可信度
   *
   * @param faceId1 第一个Face的face_id
   * @param faceId2 第二个Face的face_id
   * @return
   */
  public static Call<MatchCompare> matchCompare(String faceId1, String faceId2) {
    Call<MatchCompare> call = sEyekeyManagerService.matchCompare(sAppId, sAppKey, faceId1, faceId2);
    sCalls.add(call);
    return call;
  }

  /**
   * 由第三方应用服务端发起，向EYEKEY平台确认应用客户端获取的动态是否有效。
   * 若第三方应用调用认证match_verify接口由Server端发起并获得动态码，则可以不用调用此接口。
   * 若第三方应用的动态码有效则返回true，否则返回false。
   *
   * @param dynamicode 第三方应用获取的动态码
   * @param peopleName 对应的People
   * @return
   */
  public static Call<MatchConfirm> matchConfirm(String dynamicode, String peopleName) {
    Call<MatchConfirm> call = sEyekeyManagerService.matchConfirm(sAppId, sAppKey, dynamicode, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * 给定一个Face和一个People，返回是否是同一个人的判断以及可信度
   *
   * @param faceId     待验证的face_id
   * @param peopleName 对应的People
   * @return
   */
  public static Call<MatchVerify> matchVerify(String faceId, String peopleName) {
    Call call = sEyekeyManagerService.matchVerify(sAppId, sAppKey, faceId, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * 给定一个Face和一个Facegather，在Facegather内搜索最相似的Face
   *
   * @param faceId         待搜索的Face的face_id
   * @param faceGatherName 指定搜索范围为此Facegather
   * @param count          (可选)表示一共获取不超过count 个 搜索结果。默认count=3，选择相似度最高的三个face返回
   * @return
   */
  public static Call<MatchSearch> matchSearch(String faceId, String faceGatherName, int count) {
    Call<MatchSearch> call = sEyekeyManagerService.matchSearch(sAppId, sAppKey, faceId, faceGatherName, count);
    sCalls.add(call);
    return call;
  }

  /**
   * 对于一个待查询的Face列表（或者对于给定的Image中所有的Face），在一个Crowd中查询最相似的People
   *
   * @param crowdName 识别候选人组成的Crowd
   * @param faceId    开发者也可以指定一个face_id的列表来对这些crowd进行识别。
   *                  可以设置此参数face_id为一个逗号隔开的face_id列表
   * @return
   */
  public static Call<MatchIdentify> matchIdentify(String crowdName, String faceId) {
    Call<MatchIdentify> call = sEyekeyManagerService.matchIdentify(sAppId, sAppKey, crowdName, faceId);
    sCalls.add(call);
    return call;
  }

  /**
   * 创建一个facegather
   *
   * @param faceGatherName (可选)facegather的Name信息，必须在App中全局唯一。Name不能包含^@,&=*
   *                       '"等非法字符，且长度不得超过255。Name也可以不指定，此时系统将产生一个随机的name。
   * @param faceId         (可选)一组用逗号分隔的face_id, 表示将这些Face加入到该facegather中
   * @param tip            (可选)facegather相关的tip，不需要全局唯一，不能包含^@,&=*'"等非法字符，长度不能超过255。
   * @return
   */
  public static Call<FaceGatherCreate> faceGatherCreate(String faceGatherName, String faceId, String tip) {
    Call<FaceGatherCreate> call = sEyekeyManagerService.faceGatherCreate(sAppId, sAppKey, faceGatherName, faceId, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除facegather
   *
   * @param faceGatherName 用逗号隔开的待删除的facegatherid列表或者name列表
   * @return
   */
  public static Call<FaceGatherDelete> facegatherDelete(String faceGatherName) {
    Call<FaceGatherDelete> call = sEyekeyManagerService.facegatherDelete(sAppId, sAppKey, faceGatherName);
    sCalls.add(call);
    return call;
  }

  /**
   * 将一组Face加入到一个facegather中
   *
   * @param faceGatherName 相应facegather的name或者id
   * @param faceId         一组用逗号分隔的face_id,表示将这些Face加入到相应facegather中
   * @return
   */
  public static Call<FaceGatherAddFace> faceGatherAddFace(String faceGatherName, String faceId) {
    Call<FaceGatherAddFace> call = sEyekeyManagerService.faceGatherAddFace(sAppId, sAppKey, faceGatherName, faceId);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除facegather中的一个或多个Face
   *
   * @param faceGatherName 相应facegather的name或者id
   * @param faceId         一组用逗号分隔的face_id列表，表示将这些face从该facegather中删除。开发者也可以指定face_id=all
   *                       , 表示删除该facegather所有相关Face
   * @return
   */
  public static Call<FaceGatherRemoveFace> faceGatherRemoveface(String faceGatherName, String faceId) {
    Call<FaceGatherRemoveFace> call = sEyekeyManagerService.faceGatherRemoveFace(sAppId, sAppKey, faceGatherName, faceId);
    sCalls.add(call);
    return call;
  }

  /**
   * 设置facegather的name和tip
   *
   * @param faceGatherName 相应facegather的name或者id
   * @param name           (可选)新的name
   * @param tip            (可选)新的tip
   * @return
   */
  public static Call<FaceGatherSet> faceGatherSet(String faceGatherName, String name, String tip) {
    Call<FaceGatherSet> call = sEyekeyManagerService.faceGatherSet(sAppId, sAppKey, faceGatherName, name, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 获取一个facegather的信息, 包括name, id, tip, 相关的face等信息
   *
   * @param faceGatherName 相应facegather的id或name
   * @return
   */
  public static Call<FaceGatherGet> faceGatherGet(String faceGatherName) {
    Call<FaceGatherGet> call = sEyekeyManagerService.faceGatherGet(sAppId, sAppKey, faceGatherName);
    sCalls.add(call);
    return call;
  }

  /**
   * 创建一个People
   *
   * @param peopleName (可选)People的Name信息，必须在App中全局唯一。Name不能包含^@,&=*
   *                   '"等非法字符，且长度不得超过255。Name也可以不指定，此时系统将产生一个随机的name
   * @param faceId     (可选)一组用逗号分隔的face_id, 表示将这些Face加入到该People中
   * @param tip        (可选)People相关的tip，不需要全局唯一，不能包含^@,&=*'"等非法字符，长度不能超过255
   * @param crowdName  (可选)一组用逗号分割的crowd id列表或者crowd
   *                   name列表。如果该参数被指定，该People被create之后就会被加入到这些组中
   * @return
   */
  public static Call<PeopleCreate> peopleCreate(String faceId, String peopleName, String tip, String crowdName) {
    Call<PeopleCreate> call = sEyekeyManagerService.peopleCreate(sAppId, sAppKey, faceId, peopleName, tip, crowdName);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除一组People
   *
   * @param peopleName 用逗号隔开的待删除的People name列表
   * @return
   */
  public static Call<PeopleDelete> peopleDeleteByName(String peopleName) {
    Call<PeopleDelete> call = sEyekeyManagerService.peopleDeleteByName(sAppId, sAppKey, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除一组People
   *
   * @param peopleId 用逗号隔开的待删除的People id列表
   * @return
   */
  public static Call<PeopleDelete> peopleDeleteById(String peopleId) {
    Call<PeopleDelete> call = sEyekeyManagerService.peopleDeleteById(sAppId, sAppKey, peopleId);
    sCalls.add(call);
    return call;
  }

  /**
   * ·将一组Face加入到一个People中。注意，一个Face只能被加入到一个People中。 ·一个People最多允许包含10000个Face
   *
   * @param faceId     一组用逗号分隔的face_id,表示将这些Face加入到相应People中。
   * @param peopleName 相应People的name
   * @return
   */
  public static Call<PeopleAdd> peopleAddByName(String faceId, String peopleName) {
    Call<PeopleAdd> call = sEyekeyManagerService.peopleAddByName(sAppId, sAppKey, faceId, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * ·将一组Face加入到一个People中。注意，一个Face只能被加入到一个People中。 ·一个People最多允许包含10000个Face
   *
   * @param faceId   一组用逗号分隔的face_id,表示将这些Face加入到相应People中。
   * @param peopleId 相应People的id
   * @return
   */
  public static Call<PeopleAdd> peopleAddById(String faceId, String peopleId) {
    Call<PeopleAdd> call = sEyekeyManagerService.peopleAddById(sAppId, sAppKey, faceId, peopleId);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除People中的一个或多个Face
   *
   * @param peopleName 相应People的name
   * @param faceId     可传入一组用逗号分隔的face_id列表，表示将这些face从指定的People中删除。开发者也可以指定face_id=
   *                   all, 表示删除People所有相关Face。
   * @return
   */
  public static Call<PeopleRemove> peopleRemoveByName(String peopleName, String faceId) {
    Call<PeopleRemove> call = sEyekeyManagerService.peopleRemoveByName(sAppId, sAppKey, peopleName, faceId);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除People中的一个或多个Face
   *
   * @param peopleId 相应People的id
   * @param faceId   可传入一组用逗号分隔的face_id列表，表示将这些face从指定的People中删除。开发者也可以指定face_id=
   *                 all, 表示删除People所有相关Face。
   * @return
   */
  public static Call<PeopleRemove> peopleRemoveById(String peopleId, String faceId) {
    Call<PeopleRemove> call = sEyekeyManagerService.peopleRemoveById(sAppId, sAppKey, peopleId, faceId);
    sCalls.add(call);
    return call;
  }

  /**
   * 设置People的name和tip
   *
   * @param peopleName 相应People的name
   * @param name       新的name(可选)
   * @param tip        新的tip(可选)
   * @return
   */
  public static Call<PeopleSet> peopleSetByName(String peopleName, String name, String tip) {
    Call<PeopleSet> call = sEyekeyManagerService.peopleSetByName(sAppId, sAppKey, peopleName, name, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 设置People的name和tip
   *
   * @param peopleId 相应People的id
   * @param name     新的name(可选)
   * @param tip      新的tip(可选)
   * @return
   */
  public static Call<PeopleSet> peopleSetById(String peopleId, String name, String tip) {
    Call<PeopleSet> call = sEyekeyManagerService.peopleSetById(sAppId, sAppKey, peopleId, name, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 获取一个People的信息, 包括name, id, tip, 相关的face, 以及crowds等信息
   *
   * @param peopleName 相应People的name
   * @return
   */
  public static Call<PeopleGet> peopleGetByName(String peopleName) {
    Call<PeopleGet> call = sEyekeyManagerService.peopleGetByName(sAppId, sAppKey, peopleName, "face");
    sCalls.add(call);
    return call;
  }

  /**
   * 获取一个People的信息, 包括name, id, tip, 相关的face, 以及crowds等信息
   *
   * @param peopleId 相应People的id
   * @return
   */
  public static Call<PeopleGet> peopleGetById(String peopleId) {
    Call<PeopleGet> call = sEyekeyManagerService.peopleGetById(sAppId, sAppKey, peopleId, "face");
    sCalls.add(call);
    return call;
  }

  /**
   * ·创建一个Crowd。开发板Crowd最多允许包含100个People。开发版最多允许创建5个crowd。
   *
   * @param crowdName  (可选)Crowd的Name信息，必须在App中全局唯一。Name不能包含^@,&=*
   *                   '"等非法字符，且长度不得超过255。Name也可以不指定，此时系统将产生一个随机的name
   * @param peopleName (可选)一组用逗号分隔的people_name,
   *                   表示将这些People加入到该Crowd中。注意，一个People可以被加入到多个Crowd中。
   * @param tip        (可选)Crowd的tip，不需要全局唯一，不能包含^@,&=*'"等非法字符，长度不能超过255
   * @return
   */
  public static Call<CrowdCreate> crowdCreateWithName(String crowdName, String peopleName, String tip) {
    Call<CrowdCreate> call = sEyekeyManagerService.crowdCreateWithName(sAppId, sAppKey, crowdName, peopleName, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * ·创建一个Crowd。开发板Crowd最多允许包含100个People。开发版最多允许创建5个crowd。
   *
   * @param crowdName (可选)Crowd的Name信息，必须在App中全局唯一。Name不能包含^@,&=*
   *                  '"等非法字符，且长度不得超过255。Name也可以不指定，此时系统将产生一个随机的name
   * @param peopleId  (可选)一组用逗号分隔的people_id,
   *                  表示将这些People加入到该Crowd中。注意，一个People可以被加入到多个Crowd中。
   * @param tip       (可选)Crowd的tip，不需要全局唯一，不能包含^@,&=*'"等非法字符，长度不能超过255
   * @return
   */
  public static Call<CrowdCreate> crowdCreateWithId(String crowdName, String peopleId, String tip) {
    Call<CrowdCreate> call = sEyekeyManagerService.crowdCreateWithId(sAppId, sAppKey, crowdName, peopleId, tip);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除一组Crowd。
   *
   * @param crowdName 一组用逗号分割的crowd_name，表示删除这些Crowd
   * @return
   */
  public static Call<CrowdDelete> crowdDeleteByName(String crowdName) {
    Call<CrowdDelete> call = sEyekeyManagerService.crowdDeleteByName(sAppId, sAppKey, crowdName);
    sCalls.add(call);
    return call;
  }

  /**
   * 删除一组Crowd。
   *
   * @param crowdId 一组用逗号分割的crowd_id，表示删除这些Crowd
   * @return
   */
  public static Call<CrowdDelete> crowdDeleteById(String crowdId) {
    Call<CrowdDelete> call = sEyekeyManagerService.crowdDeleteById(sAppId, sAppKey, crowdId);
    sCalls.add(call);
    return call;
  }

  /**
   * 从Crowd中删除一个或一组People。
   *
   * @param crowdName  Crowd的id或name。
   * @param peopleName 一组用逗号分割的people_id列表或者people_name列表，表示将这些people从该Crowd中删除。
   *                   开发者也可以指定people_id=all, 表示删掉该Crowd中所有People。
   * @return
   */
  public static Call<CrowdAddAndRemove> crowdRemove(String crowdName, String peopleName) {
    Call<CrowdAddAndRemove> call = sEyekeyManagerService.crowdRemove(sAppId, sAppKey, crowdName, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * 将一组People加入到一个Crowd中。一个Crowd最多允许包含10000个People。
   *
   * @param crowdName  相应Crowd的id或name
   * @param peopleName 一组用逗号分隔的people_id或people_name，表示将这些People加入到相应Crowd中。
   * @return
   */
  public static Call<CrowdAddAndRemove> crowdAdd(String crowdName, String peopleName) {
    Call<CrowdAddAndRemove> call = sEyekeyManagerService.crowdAdd(sAppId, sAppKey, crowdName, peopleName);
    sCalls.add(call);
    return call;
  }

  /**
   * 获取Crowd的信息，包括Crowd中的People列表，Crowd的tip等信息。
   *
   * @param crowdName 待查询Crowd的name
   *                  此时将返回所有未加入任何Crowd的People。
   * @return
   */
  public static Call<CrowdGet> crowdGetByName(String crowdName) {
    Call<CrowdGet> call = sEyekeyManagerService.crowdGetByName(sAppId, sAppKey, crowdName);
    sCalls.add(call);
    return call;
  }

  /**
   * 获取Crowd的信息，包括Crowd中的People列表，Crowd的tip等信息。
   *
   * @param crowdId 待查询Crowd的id。开发者也可以指定crowd_id=none，
   *                此时将返回所有未加入任何Crowd的People。
   * @return
   */
  public static Call<CrowdGet> crowdGetById(String crowdId) {
    Call<CrowdGet> call = sEyekeyManagerService.crowdGetById(sAppId, sAppKey, crowdId);
    sCalls.add(call);
    return call;
  }

  /**
   * 获取该App相关的信息。
   *
   * @return
   */
  public static Call<AppInfo> getAppInfo() {
    Call<AppInfo> call = sEyekeyManagerService.getAppinfo(sAppId, sAppKey);
    sCalls.add(call);
    return call;
  }

  public static void cancelAllCall() {
    for (Call call : sCalls) {
      if (call != null) {
        call.cancel();
      }
    }
    sCalls.clear();
  }

  public interface EyekeyService {

    @FormUrlEncoded
    @POST(Constant.Check + "/checking")
    Call<FaceAttrs> checkingImageBase64(
        @Field("app_id") String appId,
        @Field("app_key") String appKey,
        @Field("img") String base64,
        @Field("mode") String mode,
        @Field("tip") String tip
    );

    @GET(Constant.Check + "/checking")
    Call<FaceAttrs> checkingImageUrl(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("url") String url,
        @Query("mode") String mode,
        @Query("tip") String tip
    );

    @GET(Constant.Check + "/check_mark")
    Call<FaceMark> checkMark(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("type") String type
    );

    @GET(Constant.Match + "/match_compare")
    Call<MatchCompare> matchCompare(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id1") String faceId1,
        @Query("face_id2") String faceId2
    );

    @GET(Constant.Match + "/match_confirm")
    Call<MatchConfirm> matchConfirm(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("dynamicode") String dynamicode,
        @Query("people_name") String peopleName

    );

    @GET(Constant.Match + "/match_verify")
    Call<MatchVerify> matchVerify(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("people_name") String peopleName
    );

    @GET(Constant.Match + "/match_search")
    Call<MatchSearch> matchSearch(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("facegather_name") String faceGatherName,
        @Query("count") int count
    );

    @GET(Constant.Match + "/match_identify")
    Call<MatchIdentify> matchIdentify(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName,
        @Query("face_id") String faceId
    );

    @GET(Constant.FaceGather + "/facegather_create")
    Call<FaceGatherCreate> faceGatherCreate(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName,
        @Query("face_id") String faceId,
        @Query("tip") String tip
    );

    @GET(Constant.FaceGather + "/facegather_delete")
    Call<FaceGatherDelete> facegatherDelete(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName

    );

    @GET(Constant.FaceGather + "/facegather_addface")
    Call<FaceGatherAddFace> faceGatherAddFace(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName,
        @Query("face_id") String faceId
    );

    @GET(Constant.FaceGather + "/facegather_removeface")
    Call<FaceGatherRemoveFace> faceGatherRemoveFace(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName,
        @Query("face_id") String faceId
    );

    @GET(Constant.FaceGather + "/facegather_set")
    Call<FaceGatherSet> faceGatherSet(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName,
        @Query("name") String name,
        @Query("tip") String tip
    );

    @GET(Constant.FaceGather + "/facegather_get")
    Call<FaceGatherGet> faceGatherGet(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("facegather_name") String faceGatherName
    );

    @GET(Constant.People + "/people_create")
    Call<PeopleCreate> peopleCreate(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("people_name") String peopleName,
        @Query("tip") String tip,
        @Query("crowd_name") String crowdName
    );


    @GET(Constant.People + "/people_delete")
    Call<PeopleDelete> peopleDeleteByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_name") String peopleName
    );

    @GET(Constant.People + "/people_delete")
    Call<PeopleDelete> peopleDeleteById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_id") String peopleId
    );

    @GET(Constant.People + "/people_add")
    Call<PeopleAdd> peopleAddByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("people_name") String peopleName
    );

    @GET(Constant.People + "/people_add")
    Call<PeopleAdd> peopleAddById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("face_id") String faceId,
        @Query("people_id") String peopleId
    );

    @GET(Constant.People + "/people_remove")
    Call<PeopleRemove> peopleRemoveByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_name") String peopleName,
        @Query("face_id") String faceId
    );

    @GET(Constant.People + "/people_remove")
    Call<PeopleRemove> peopleRemoveById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_id") String peopleId,
        @Query("face_id") String faceId
    );

    @GET(Constant.People + "/people_set")
    Call<PeopleSet> peopleSetByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_name") String peopleName,
        @Query("name") String name,
        @Query("tip") String tip
    );

    @GET(Constant.People + "/people_set")
    Call<PeopleSet> peopleSetById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_id") String peopleId,
        @Query("name") String name,
        @Query("tip") String tip
    );

    @GET(Constant.People + "/people_get")
    Call<PeopleGet> peopleGetByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_name") String peopleName,
        @Query("type") String type
    );

    @GET(Constant.People + "/people_get")
    Call<PeopleGet> peopleGetById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("people_id") String peopleId,
        @Query("type") String type
    );

    @GET(Constant.Crowd + "/crowd_create")
    Call<CrowdCreate> crowdCreateWithName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName,
        @Query("people_name") String peopleName,
        @Query("tip") String tip
    );

    @GET(Constant.Crowd + "/crowd_create")
    Call<CrowdCreate> crowdCreateWithId(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName,
        @Query("people_id") String peopleId,
        @Query("tip") String tip
    );

    @GET(Constant.Crowd + "/crowd_delete")
    Call<CrowdDelete> crowdDeleteByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName
    );

    @GET(Constant.Crowd + "/crowd_delete")
    Call<CrowdDelete> crowdDeleteById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_id") String crowdId
    );

    @GET(Constant.Crowd + "/crowd_remove")
    Call<CrowdAddAndRemove> crowdRemove(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName,
        @Query("people_name") String peopleName
    );

    @GET(Constant.Crowd + "/crowd_add")
    Call<CrowdAddAndRemove> crowdAdd(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName,
        @Query("people_name") String peopleName
    );

    @GET(Constant.Crowd + "/crowd_get")
    Call<CrowdGet> crowdGetByName(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_name") String crowdName
    );

    @GET(Constant.Crowd + "/crowd_get")
    Call<CrowdGet> crowdGetById(
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("crowd_id") String crowdId
    );

    @GET(Constant.Info + "/get_appinfo")
    Call<AppInfo> getAppinfo(
        @Query("app_id") String appId,
        @Query("app_key") String appKey
    );
  }
}