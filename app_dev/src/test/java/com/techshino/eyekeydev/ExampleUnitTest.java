package com.techshino.eyekeydev;

import com.techshino.eyekeydev.api.CheckAPI;
import com.techshino.eyekeydev.entity.FaceAttrs;

import org.junit.Test;

import java.io.IOException;

import rx.functions.Action1;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private static final String TAG = ExampleUnitTest.class.getSimpleName();

    private String faceId = "ca5628b53ef8401e81cc67ac6558257e";

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkingImageData() throws IOException {
        CheckAPI.checkingImageData("123", null, null)
                .subscribe(new Action1<FaceAttrs>() {
                    @Override
                    public void call(FaceAttrs faceAttrs) {
                        System.out.println("checkingImageData:" + faceAttrs.toString());
                        assertEquals(faceAttrs.getRes_code(), "1067");
                    }
                });

    }

//    @Test
//    public void checkingImageUrl() throws IOException {
//        Call<FaceAttrs> call = CheckAPI.checkingImageUrl("http://d.hiphotos.baidu.com/image/pic/item/9345d688d43f87949725e4fed61b0ef41ad53a03.jpg", null, null);
//        FaceAttrs faceAttrs = call.execute().body();
//        System.out.println("checkingImageUrl:" + faceAttrs.toString());
//        checkMark(faceAttrs.getFace().get(0).getFace_id());
//        assertEquals(faceAttrs.getRes_code(), "0000");
//    }
//
//    public void checkMark(String faceId) throws IOException {
//        Call<FaceMark> call = CheckAPI.checkMark(faceId, "");
//        FaceMark faceMark = call.execute().body();
//        System.out.println("checkMark:" + faceMark);
//        assertEquals(faceMark.getRes_code(), "0000");
//    }
//
//    @Test
//    public void matchCompare() throws IOException {
//        Call<MatchCompare> call = CheckAPI.matchCompare(faceId, faceId);
//        MatchCompare matchCompare = call.execute().body();
//        System.out.println("matchCompare:" + matchCompare);
//        assertEquals("0000", matchCompare.getRes_code());
//    }
//
//    @Test
//    public void matchConfirm() throws IOException {
//        Call<MatchConfirm> call = CheckAPI.matchConfirm("123", "123");
//        MatchConfirm matchConfirm = call.execute().body();
//        System.out.println("matchCompare:" + matchConfirm);
//        assertEquals(false, matchConfirm.isResult());
//    }
//
//    @Test
//    public void matchSearch() throws IOException {
//        Call<MatchSearch> call = CheckAPI.matchSearch(faceId, "123", 3);
//        MatchSearch matchSearch = call.execute().body();
//        System.out.println("matchSearch:" + matchSearch);
//    }
//
//    @Test
//    public void matchIdentify() throws IOException {
//        Call<MatchIdentify> call = CheckAPI.matchIdentify("123", faceId);
//        MatchIdentify str = call.execute().body();
//        System.out.println("matchIdentify:" + str);
//        assertEquals("1019", str.getRes_code());
//    }
//
//    @Test
//    public void faceGatherCreate() throws IOException {
//        Call<FaceGatherCreate> call = CheckAPI.faceGatherCreate("123", faceId, "");
//        FaceGatherCreate faceGatherCreate = call.execute().body();
//        System.out.println("faceGatherCreate:" + faceGatherCreate);
//    }
//
//    @Test
//    public void faceGatherDelete() throws IOException {
//        Call<FaceGatherDelete> call = CheckAPI.facegatherDelete("123");
//        FaceGatherDelete obj = call.execute().body();
//        System.out.println("facegatherDelete:" + obj);
//    }
//
//    @Test
//    public void faceGatherAddFace() throws IOException {
//        Call<FaceGatherAddFace> call = CheckAPI.faceGatherAddFace("123", faceId);
//        FaceGatherAddFace obj = call.execute().body();
//        System.out.println("faceGatherAddFace:" + obj);
//    }
//
//    @Test
//    public void faceGatherRemoveface() throws IOException {
//        Call<FaceGatherRemoveFace> call = CheckAPI.faceGatherRemoveface("123", faceId);
//        FaceGatherRemoveFace obj = call.execute().body();
//        System.out.println("faceGatherRemoveface:" + obj);
//    }
//
//    @Test
//    public void faceGatherSet() throws IOException {
//        Call<FaceGatherSet> call = CheckAPI.faceGatherSet("123", "12", "");
//        FaceGatherSet obj = call.execute().body();
//        System.out.println("faceGatherSet:" + obj);
//    }
//
//    @Test
//    public void faceGatherGet() throws IOException {
//        Call<FaceGatherGet> call = CheckAPI.faceGatherGet("123");
//        FaceGatherGet obj = call.execute().body();
//        System.out.println("faceGatherGet:" + obj);
//    }
//
//    @Test
//    public void peopleRemove() throws IOException {
//        Call<PeopleRemove> call = CheckAPI.peopleRemove("123", faceId);
//        PeopleRemove obj = call.execute().body();
//        System.out.println("peopleRemove:" + obj);
//    }
//
//    @Test
//    public void peopleSet() throws IOException {
//        Call<PeopleSet> call = CheckAPI.peopleSet("321", "123", "");
//        PeopleSet obj = call.execute().body();
//        System.out.println("peopleSet:" + obj);
//    }
//
//    @Test
//    public void crowdCreate() throws IOException {
//        Call<CrowdCreate> call = CheckAPI.crowdCreate("123", "123", "");
//        CrowdCreate obj = call.execute().body();
//        System.out.println("crowdCreate:" + obj);
//    }
//
//
//    @Test
//    public void crowdDelete() throws IOException {
//        Call<CrowdDelete> call = CheckAPI.crowdDelete("123");
//        CrowdDelete obj = call.execute().body();
//        System.out.println("crowdDelete:" + obj);
//    }
//
//    @Test
//    public void crowdRemove() throws IOException {
//        Call<CrowdAddAndRemove> call = CheckAPI.crowdRemove("123", "123");
//        CrowdAddAndRemove obj = call.execute().body();
//        System.out.println("crowdRemove:" + obj);
//    }
//
//    @Test
//    public void crowdAdd() throws IOException {
//        Call<CrowdAddAndRemove> call = CheckAPI.crowdAdd("123", "123");
//        CrowdAddAndRemove obj = call.execute().body();
//        System.out.println("crowdAdd:" + obj);
//    }
//
//    @Test
//    public void crowdGet() throws IOException {
//        Call<CrowdGet> call = CheckAPI.crowdGet("123");
//        CrowdGet obj = call.execute().body();
//        System.out.println("crowdGet:" + obj);
//    }
//
//    @Test
//    public void getAppInfo() throws IOException {
//        Call<AppInfo> call = CheckAPI.getAppInfo();
//        AppInfo obj = call.execute().body();
//        System.out.println("getAppInfo:" + obj);
//    }
}