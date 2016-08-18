package com.xiaozhi.lvm.demo

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import butterknife.ButterKnife
import butterknife.OnClick
import com.techshino.eyekeysdk.api.CheckAPI
import com.techshino.eyekeysdk.entity.CrowdCreate
import com.techshino.eyekeysdk.entity.PeopleGet
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

  private val dirPath = "boss"

  private val faceIds: String? = null

  private var peopleIds: String? = null

  private val peoples = ArrayList<People>()
  private var mCrowdName: String? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ButterKnife.bind(this)

    // 创建crowd
    mCrowdName = "绿盟大佬"
    CheckAPI.crowdCreate(mCrowdName, "", null)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<CrowdCreate>() {
          override fun onCompleted() {
            Log.d(TAG, "onCompleted")
          }

          override fun onError(e: Throwable) {
            Log.d(TAG, "onError")
          }

          override fun onNext(crowdCreate: CrowdCreate) {
            Log.d(TAG, crowdCreate.toString())
          }
        })
  }

  @OnClick(R.id.getFaceIdBtn)
  internal fun getFaceIdBtnClick() {
    Observable.just(PATH)
        .flatMap { s -> Observable.from<File>(File(s).listFiles()) }
        .flatMap { file -> getFaceIds(file) }
        .flatMap { people -> createPeople(people) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<String>() {
          override fun onCompleted() {
            Log.d(TAG, "onCompleted...")
            Log.d(TAG, "peopleIds================" + peopleIds!!)
          }

          override fun onError(e: Throwable) {
            Log.d(TAG, "onError...")
          }

          override fun onNext(s: String) {
            Log.i(TAG, "onNext...")
            peopleIds += s + ","
        }
        })
  }

  @OnClick(R.id.getCrowdInfoBtn)
  internal fun getCrowdInfoBtn() {
    CheckAPI.crowdGet(mCrowdName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { crowdGet -> Log.i(TAG, crowdGet.toString()) }
  }

  @OnClick(R.id.getPeopleBtn)
  internal fun getPeopleBtnClick() {
    CheckAPI.peopleGet("马云.jpg")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Subscriber<PeopleGet>() {
          override fun onCompleted() {
            Log.d(TAG, "onCompleted...")
          }

          override fun onError(e: Throwable) {
            Log.d(TAG, "onError..." + e.message)
          }

          override fun onNext(peopleGet: PeopleGet) {
            Log.d(TAG, "onNext..." + peopleGet.toString())
          }
        })
  }

  @OnClick(R.id.setCrowdFaceBtn)
  internal fun setCrowdFaceBtn() {
    CheckAPI.crowdAdd("绿盟大佬", faceIds)
  }

  private fun getFaceIds(file: File): Observable<People> {
    return CheckAPI.checkingImageByBase64(encodeBase64File(file), null, null)
        .map { faceAttrs -> People(file.name, null!!, faceAttrs.getFace().get(0).getFace_id()) }
  }

  private fun createPeople(p: People): Observable<String> {
    return CheckAPI.peopleCreate(p.faceId, p.name, "", "绿盟大佬")
        .map { peopleCreate ->
          Log.i(TAG, "peopleCreate:" + peopleCreate.getPeople_id())
          peopleCreate.getPeople_id()
        }
  }

  fun encodeBase64File(file: File): String {
    var inputFile: FileInputStream? = null
    var buffer: ByteArray? = null
    try {
      inputFile = FileInputStream(file)
      buffer = ByteArray(file.length().toInt())
      inputFile.read(buffer)
      inputFile.close()
    } catch (e: IOException) {
      e.printStackTrace()
      return ""
    }

    return Base64.encodeToString(buffer, Base64.DEFAULT)
  }

  companion object {

    private val TAG = MainActivity::class.java.simpleName

    private val PATH = Environment.getExternalStorageDirectory().path + "/"
  }
}
