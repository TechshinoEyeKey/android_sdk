package com.techshino.eyekeydemo.api;

import com.techshino.eyekeydemo.conn.Constant;
import com.techshino.eyekeydemo.entity.AppInfo;
import com.techshino.eyekeydemo.entity.CrowdAddAndRemove;
import com.techshino.eyekeydemo.entity.CrowdCreate;
import com.techshino.eyekeydemo.entity.CrowdDelete;
import com.techshino.eyekeydemo.entity.CrowdGet;
import com.techshino.eyekeydemo.entity.FaceAttrs;
import com.techshino.eyekeydemo.entity.FaceGatherAddFace;
import com.techshino.eyekeydemo.entity.FaceGatherCreate;
import com.techshino.eyekeydemo.entity.FaceGatherDelete;
import com.techshino.eyekeydemo.entity.FaceGatherGet;
import com.techshino.eyekeydemo.entity.FaceGatherRemoveFace;
import com.techshino.eyekeydemo.entity.FaceGatherSet;
import com.techshino.eyekeydemo.entity.FaceMark;
import com.techshino.eyekeydemo.entity.MatchCompare;
import com.techshino.eyekeydemo.entity.MatchConfirm;
import com.techshino.eyekeydemo.entity.MatchIdentify;
import com.techshino.eyekeydemo.entity.MatchSearch;
import com.techshino.eyekeydemo.entity.MatchVerify;
import com.techshino.eyekeydemo.entity.PeopleAdd;
import com.techshino.eyekeydemo.entity.PeopleCreate;
import com.techshino.eyekeydemo.entity.PeopleDelete;
import com.techshino.eyekeydemo.entity.PeopleGet;
import com.techshino.eyekeydemo.entity.PeopleRemove;
import com.techshino.eyekeydemo.entity.PeopleSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.schedulers.Schedulers;

public class CheckAPI implements Constant {

    private static final String TAG = "CheckAPI";

    private static ArrayList<Call> sCalls = new ArrayList<>();

    private static final Retrofit sRetrofit = new Retrofit.Builder()
            .baseUrl(API_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    public static final EyekeyService sEyekeyManagerService = sRetrofit.create(EyekeyService.class);

    /**
     * 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性
     *
     * @param dataImage 待检测图片数据信息,通过POST方法上传的二进制数据，原始图片大小需要小于3M
     * @param mode      (可选)检测模式 (默认) oneface
     *                  。在oneface模式中，检测器仅找出图片中最大的一张脸。如果图中有多张人脸大小相同，随机返回一张人脸信息。
     * @param tip       (可选)指定一个不包含^@,&=*'"等非法字符且不超过255字节的字符串作为tip
     */
    public static Observable<FaceAttrs> checkingImageData(String dataImage, String mode, String tip) {
        return sEyekeyManagerService.checkingImageData(APP_ID, APP_KEY, dataImage, mode, tip);
    }

    /**
     * 检测给定图片(Image)中的所有人脸(Face)的位置和相应的面部属性
     *
     * @param urlImage 待检测图片url
     * @param mode     (可选)检测模式 (默认) oneface
     *                 。在oneface模式中，检测器仅找出图片中最大的一张脸。如果图中有多张人脸大小相同，随机返回一张人脸信息。
     * @param tip      (可选)指定一个不包含^@,&=*'"等非法字符且不超过255字节的字符串作为tip
     * @return
     */
    public static Observable<FaceAttrs> checkingImageUrl(String urlImage, String mode, String tip) {
        return sEyekeyManagerService.checkingImageUrl(APP_ID, APP_KEY, urlImage, mode, tip);
    }

    /**
     * 检测给定人脸(Face)相应的面部轮廓，五官等关键点的位置，包括49点一种模式。
     *
     * @param face_id 待检测人脸的face_id
     * @param type    (可选)表示返回的关键点个数，目前支持49p, 默认为49p
     * @return
     */
    public static Observable<FaceMark> checkMark(String face_id, String type) {
        return sEyekeyManagerService.checkMark(APP_ID, APP_KEY, face_id, type);
    }

    /**
     * 计算两个Face的相似性以及五官可信度
     *
     * @param faceId1 第一个Face的face_id
     * @param faceId2 第二个Face的face_id
     * @return
     */
    public static Observable<MatchCompare> matchCompare(String faceId1, String faceId2) {
        return sEyekeyManagerService.matchCompare(APP_ID, APP_KEY, faceId1, faceId2);
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
    public static Observable<MatchConfirm> matchConfirm(String dynamicode, String peopleName) {
        return sEyekeyManagerService.matchConfirm(APP_ID, APP_KEY, dynamicode, peopleName);
    }

    /**
     * 给定一个Face和一个People，返回是否是同一个人的判断以及可信度
     *
     * @param faceId     待验证的face_id
     * @param peopleName 对应的People
     * @return
     */
    public static Observable<MatchVerify> matchVerify(String faceId, String peopleName) {
        return sEyekeyManagerService.matchVerify(APP_ID, APP_KEY, faceId, peopleName).subscribeOn(Schedulers.io());
    }

    /**
     * 给定一个Face和一个Facegather，在Facegather内搜索最相似的Face
     *
     * @param faceId         待搜索的Face的face_id
     * @param faceGatherName 指定搜索范围为此Facegather
     * @param count          (可选)表示一共获取不超过count 个 搜索结果。默认count=3，选择相似度最高的三个face返回
     * @return
     */
    public static Observable<MatchSearch> matchSearch(String faceId, String faceGatherName, int count) {
        return sEyekeyManagerService.matchSearch(APP_ID, APP_KEY, faceId, faceGatherName, count);
    }

    /**
     * 对于一个待查询的Face列表（或者对于给定的Image中所有的Face），在一个Crowd中查询最相似的People
     *
     * @param crowdName 识别候选人组成的Crowd
     * @param faceId    开发者也可以指定一个face_id的列表来对这些crowd进行识别。
     *                  可以设置此参数face_id为一个逗号隔开的face_id列表
     * @return
     */
    public static Observable<MatchIdentify> matchIdentify(String crowdName, String faceId) {
        return sEyekeyManagerService.matchIdentify(APP_ID, APP_KEY, crowdName, faceId);
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
    public static Observable<FaceGatherCreate> faceGatherCreate(String faceGatherName, String faceId, String tip) {
        return sEyekeyManagerService.faceGatherCreate(APP_ID, APP_KEY, faceGatherName, faceId, tip);
    }

    /**
     * 删除facegather
     *
     * @param faceGatherName 用逗号隔开的待删除的facegatherid列表或者name列表
     * @return
     */
    public static Observable<FaceGatherDelete> facegatherDelete(String faceGatherName) {
        return sEyekeyManagerService.facegatherDelete(APP_ID, APP_KEY, faceGatherName);
    }

    /**
     * 将一组Face加入到一个facegather中
     *
     * @param faceGatherName 相应facegather的name或者id
     * @param faceId         一组用逗号分隔的face_id,表示将这些Face加入到相应facegather中
     * @return
     */
    public static Observable<FaceGatherAddFace> faceGatherAddFace(String faceGatherName, String faceId) {
        return sEyekeyManagerService.faceGatherAddFace(APP_ID, APP_KEY, faceGatherName, faceId);
    }

    /**
     * 删除facegather中的一个或多个Face
     *
     * @param faceGatherName 相应facegather的name或者id
     * @param faceId         一组用逗号分隔的face_id列表，表示将这些face从该facegather中删除。开发者也可以指定face_id=all
     *                       , 表示删除该facegather所有相关Face
     * @return
     */
    public static Observable<FaceGatherRemoveFace> faceGatherRemoveface(String faceGatherName, String faceId) {
        return sEyekeyManagerService.faceGatherRemoveFace(APP_ID, APP_KEY, faceGatherName, faceId);
    }

    /**
     * 设置facegather的name和tip
     *
     * @param faceGatherName 相应facegather的name或者id
     * @param name           (可选)新的name
     * @param tip            (可选)新的tip
     * @return
     */
    public static Observable<FaceGatherSet> faceGatherSet(String faceGatherName, String name, String tip) {
        return sEyekeyManagerService.faceGatherSet(APP_ID, APP_KEY, faceGatherName, name, tip);
    }

    /**
     * 获取一个facegather的信息, 包括name, id, tip, 相关的face等信息
     *
     * @param faceGatherName 相应facegather的id或name
     * @return
     */
    public static Observable<FaceGatherGet> faceGatherGet(String faceGatherName) {
        return sEyekeyManagerService.faceGatherGet(APP_ID, APP_KEY, faceGatherName);
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
    public static Observable<PeopleCreate> peopleCreate(String faceId, String peopleName, String tip, String crowdName) {
        return sEyekeyManagerService.peopleCreate(APP_ID, APP_KEY, faceId, peopleName, tip, crowdName);
    }

    /**
     * 删除一组People
     *
     * @param peopleName 用逗号隔开的待删除的People id列表或者name列表
     * @return
     */
    public static Observable<PeopleDelete> peopleDelete(String peopleName) {
        return sEyekeyManagerService.peopleDelete(APP_ID, APP_KEY, peopleName).subscribeOn(Schedulers.io());
    }

    /**
     * ·将一组Face加入到一个People中。注意，一个Face只能被加入到一个People中。 ·一个People最多允许包含10000个Face
     *
     * @param faceId     一组用逗号分隔的face_id,表示将这些Face加入到相应People中。
     * @param peopleName 相应People的name或者id
     * @return
     */
    public static Observable<PeopleAdd> peopleAdd(String faceId, String peopleName) {
        return sEyekeyManagerService.peopleAdd(APP_ID, APP_KEY, faceId, peopleName).subscribeOn(Schedulers.io());
    }

    /**
     * 删除People中的一个或多个Face
     *
     * @param peopleName 相应People的name或者id
     * @param faceId     可传入一组用逗号分隔的face_id列表，表示将这些face从指定的People中删除。开发者也可以指定face_id=
     *                   all, 表示删除People所有相关Face。
     * @return
     */
    public static Observable<PeopleRemove> peopleRemove(String peopleName, String faceId) {
        return sEyekeyManagerService.peopleRemove(APP_ID, APP_KEY, peopleName, faceId);
    }

    /**
     * 设置People的name和tip
     *
     * @param peopleName 相应People的name或者id
     * @param name       新的name(可选)
     * @param tip        新的tip(可选)
     * @return
     */
    public static Observable<PeopleSet> peopleSet(String peopleName, String name, String tip) {
        return sEyekeyManagerService.peopleSet(APP_ID, APP_KEY, peopleName, name, tip);
    }

    /**
     * 获取一个People的信息, 包括name, id, tip, 相关的face, 以及crowds等信息
     *
     * @param peopleName 相应People的name或者id
     * @return
     */
    public static Observable<PeopleGet> peopleGet(String peopleName) {
        return sEyekeyManagerService.peopleGet(APP_ID, APP_KEY, peopleName);
    }

    /**
     * ·创建一个Crowd。一个Crowd最多允许包含1000个People。开发版最多允许创建5个crowd。
     *
     * @param crowdName  (可选)Crowd的Name信息，必须在App中全局唯一。Name不能包含^@,&=*
     *                   '"等非法字符，且长度不得超过255。Name也可以不指定，此时系统将产生一个随机的name
     * @param peopleName (可选)一组用逗号分隔的people_id或people_name,
     *                   表示将这些People加入到该Crowd中。注意，一个People可以被加入到多个Crowd中。
     * @param tip        (可选)Crowd的tip，不需要全局唯一，不能包含^@,&=*'"等非法字符，长度不能超过255
     * @return
     */
    public static Observable<CrowdCreate> crowdCreate(String crowdName, String peopleName, String tip) {
        return sEyekeyManagerService.crowdCreate(APP_ID, APP_KEY, crowdName, peopleName, tip);
    }

    /**
     * 删除一组Crowd。
     *
     * @param crowdName 一组用逗号分割的crowd_id或crowd_name，表示删除这些Crowd
     * @return
     */
    public static Observable<CrowdDelete> crowdDelete(String crowdName) {
        return sEyekeyManagerService.crowdDelete(APP_ID, APP_KEY, crowdName);
    }

    /**
     * 从Crowd中删除一个或一组People。
     *
     * @param crowdName  Crowd的id或name。
     * @param peopleName 一组用逗号分割的people_id列表或者people_name列表，表示将这些people从该Crowd中删除。
     *                   开发者也可以指定people_id=all, 表示删掉该Crowd中所有People。
     * @return
     */
    public static Observable<CrowdAddAndRemove> crowdRemove(String crowdName, String peopleName) {
        return sEyekeyManagerService.crowdRemove(APP_ID, APP_KEY, crowdName, peopleName);
    }

    /**
     * 将一组People加入到一个Crowd中。一个Crowd最多允许包含10000个People。
     *
     * @param crowdName  相应Crowd的id或name
     * @param peopleName 一组用逗号分隔的people_id或people_name，表示将这些People加入到相应Crowd中。
     * @return
     */
    public static Observable<CrowdAddAndRemove> crowdAdd(String crowdName, String peopleName) {
        return sEyekeyManagerService.crowdAdd(APP_ID, APP_KEY, crowdName, peopleName);
    }

    /**
     * 获取Crowd的信息，包括Crowd中的People列表，Crowd的tip等信息。
     *
     * @param crowdName 待查询Crowd的id或name。开发者也可以指定crowd_id=none，
     *                  此时将返回所有未加入任何Crowd的People。
     * @return
     */
    public static Observable<CrowdGet> crowdGet(String crowdName) {
        return sEyekeyManagerService.crowdGet(APP_ID, APP_KEY, crowdName);
    }

    /**
     * 获取该App相关的信息。
     *
     * @return
     */
    public static Observable<AppInfo> getAppInfo() {
        Observable<AppInfo> observable = sEyekeyManagerService.getAppinfo(APP_ID, APP_KEY);
        return sEyekeyManagerService.getAppinfo(APP_ID, APP_KEY);
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
        Observable<FaceAttrs> checkingImageData(
                @Field("app_id") String appId,
                @Field("app_key") String appKey,
                @Field("img") String dataImage,
                @Field("mode") String mode,
                @Field("tip") String tip
        );

        @GET(Constant.Check + "/checking")
        Observable<FaceAttrs> checkingImageUrl(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("url") String url,
                @Query("mode") String mode,
                @Query("tip") String tip
        );

        @GET(Constant.Check + "/check_mark")
        Observable<FaceMark> checkMark(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id") String faceId,
                @Query("type") String type
        );

        @GET(Constant.Match + "/match_compare")
        Observable<MatchCompare> matchCompare(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id1") String faceId1,
                @Query("face_id2") String faceId2
        );

        @GET(Constant.Match + "/match_confirm")
        Observable<MatchConfirm> matchConfirm(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("dynamicode") String dynamicode,
                @Query("people_name") String peopleName

        );

        @GET(Constant.Match + "/match_verify")
        Observable<MatchVerify> matchVerify(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id") String faceId,
                @Query("people_name") String peopleName
        );

        @GET(Constant.Match + "/match_search")
        Observable<MatchSearch> matchSearch(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id") String faceId,
                @Query("facegather_name") String faceGatherName,
                @Query("count") int count
        );

        @GET(Constant.Match + "/match_identify")
        Observable<MatchIdentify> matchIdentify(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName,
                @Query("face_id") String faceId
        );

        @GET(Constant.FaceGather + "/facegather_create")
        Observable<FaceGatherCreate> faceGatherCreate(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName,
                @Query("face_id") String faceId,
                @Query("tip") String tip
        );

        @GET(Constant.FaceGather + "/facegather_delete")
        Observable<FaceGatherDelete> facegatherDelete(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName

        );

        @GET(Constant.FaceGather + "/facegather_addface")
        Observable<FaceGatherAddFace> faceGatherAddFace(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName,
                @Query("face_id") String faceId
        );

        @GET(Constant.FaceGather + "/facegather_removeface")
        Observable<FaceGatherRemoveFace> faceGatherRemoveFace(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName,
                @Query("face_id") String faceId
        );

        @GET(Constant.FaceGather + "/facegather_set")
        Observable<FaceGatherSet> faceGatherSet(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName,
                @Query("name") String name,
                @Query("tip") String tip
        );

        @GET(Constant.FaceGather + "/facegather_get")
        Observable<FaceGatherGet> faceGatherGet(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("facegather_name") String faceGatherName
        );

        @GET(Constant.People + "/people_create")
        Observable<PeopleCreate> peopleCreate(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id") String faceId,
                @Query("people_name") String peopleName,
                @Query("tip") String tip,
                @Query("crowd_name") String crowdName
        );


        @GET(Constant.People + "/people_delete")
        Observable<PeopleDelete> peopleDelete(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("people_name") String peopleName
        );

        @GET(Constant.People + "/people_add")
        Observable<PeopleAdd> peopleAdd(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("face_id") String faceId,
                @Query("people_name") String peopleName
        );

        @GET(Constant.People + "/people_remove")
        Observable<PeopleRemove> peopleRemove(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("people_name") String peopleName,
                @Query("face_id") String faceId
        );

        @GET(Constant.People + "/people_set")
        Observable<PeopleSet> peopleSet(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("people_name") String peopleName,
                @Query("name") String name,
                @Query("tip") String tip
        );

        @GET(Constant.People + "/people_get")
        Observable<PeopleGet> peopleGet(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("people_name") String peopleName
        );

        @GET(Constant.Crowd + "/crowd_create")
        Observable<CrowdCreate> crowdCreate(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName,
                @Query("people_name") String peopleName,
                @Query("tip") String tip
        );

        @GET(Constant.Crowd + "/crowd_delete")
        Observable<CrowdDelete> crowdDelete(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName
        );

        @GET(Constant.Crowd + "/crowd_remove")
        Observable<CrowdAddAndRemove> crowdRemove(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName,
                @Query("people_name") String peopleName
        );

        @GET(Constant.Crowd + "/crowd_add")
        Observable<CrowdAddAndRemove> crowdAdd(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName,
                @Query("people_name") String peopleName
        );

        @GET(Constant.Crowd + "/crowd_get")
        Observable<CrowdGet> crowdGet(
                @Query("app_id") String appId,
                @Query("app_key") String appKey,
                @Query("crowd_name") String crowdName
        );

        @GET(Constant.Info + "/get_appinfo")
        Observable<AppInfo> getAppinfo(
                @Query("app_id") String appId,
                @Query("app_key") String appKey
        );
    }
}
