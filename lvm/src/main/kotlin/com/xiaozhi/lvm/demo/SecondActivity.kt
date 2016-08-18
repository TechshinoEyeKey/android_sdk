package com.xiaozhi.lvm.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import rx.Observable

class SecondActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_second)

    Observable.just(123)
        .map { num ->
          Log.i(TAG, "num=" + num)
          num!! + 246
          num!! + 23
        }
        .map { num -> num.toString() }
        .subscribe { s -> Log.i(TAG, s) }
  }

  companion object {

    private val TAG = SecondActivity::class.java.simpleName
  }
}
