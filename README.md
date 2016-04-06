# EyeKey_android_sdk

android sdk for [eyekey.com](http://www.eyekey.com)

Github地址：[https://github.com/TechshinoEyeKey/android_sdk](https://github.com/TechshinoEyeKey/android_sdk)

## 说明
* 该android SDK是eyekey云平台的接口封装，适用于Android端的快速开发和集成；由于平台接口都为http实现所以开发者也可自行根据项目框架调用，降低侵入性；
* 为了方便开发者使用以及跟随Google的潮流，本SDK使用了**OkHttp**和**retrofit2**等开源框架（ps：详细可下载源码观看）；

## 使用
eyekey接口封装在了lib模块中，如需引入自己的项目中将lib拷贝到android studio项目中，引入如下：
>compile project(':lib')

**AndroidManifest.xml**中设置appId和appKey
```java
<application>
    <meta-data android:name="eyekey_appkey" android:value="your appkey"/>
        <meta-data android:name="eyekey_appid" android:value="your appid"/>
</application>
```

具体接口的使用可以参照[**com.techshino.eyekeysdk.api.CheckAPI**](https://github.com/TechshinoEyeKey/android_sdk/blob/master/lib/Eyekey%20%E4%BA%BA%E8%84%B8%E8%AF%86%E5%88%AB%E4%BA%91%E5%B9%B3%E5%8F%B0%20Android%20SDK%20%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md)类中的详细说明或者参考官网详细说明[eyekey API](http://www.eyekey.com/devcenter/api/APIface.html)

## sample_check 检测人脸示例
该模块是对人脸检测接口的示例程序，开发者可以将项目自行导入AS(android studio)并查看运行效果

## sample_compare 比对人脸示例
该模块是对人脸比对接口的示例程序，开发者可以将项目自行导入AS(android studio)并查看运行效果

## app
该模块是一个基于eyekey的注册认证的应用，开发者可以参照该应用来进行相应的拍照人脸识别等开发；
![](https://raw.githubusercontent.com/TechshinoEyeKey/android_sdk/master/screenshot/app_icon1.png)

## 注：
Android SDK下载地址：[http://www.eyekey.com/devcenter/sdk/sdkdownload.html](http://www.eyekey.com/devcenter/sdk/sdkdownload.html)

如果有问题可点击链接加入群【[EyeKey人脸识别官方群](http://qm.qq.com/cgi-bin/qm/qr?k=SoeLdMhugzD5jzkl230ABqel4X3C9YFY)】

版权
=======

    Copyright 2016 eyekey, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.