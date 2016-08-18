package com.xiaozhi.lvm.demo

/**
 * Created by wangzhi on 2016/3/23.
 */
class People(var name: String, var id: String, var faceId: String) {

  override fun toString(): String {
    return "People{name='$name\', id='$id\', faceId='$faceId\'}"
  }
}
