# EyeKey_android_sdk

android sdk for [eyekey.com](http://www.eyekey.com)

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

具体接口的使用可以参照**com.techshino.eyekeysdk.api.CheckAPI**类中的详细说明或者参考官网详细说明[eyekey API](http://www.eyekey.com/devcenter/api/APIface.html)

## sample_check moudle
该模块是对人脸检测接口的示例程序，开发者可参照示例完成其他接口；

## sample_compare
该模块是对人脸比对接口的示例程序，开发者可参照示例完成其他接口；

## app
该模块是一个基于eyekey的注册认证的应用，开发者可以参照该应用来进行相应的拍照人脸识别等开发；
![](https://raw.githubusercontent.com/TechshinoEyeKey/android_sdk/master/screenshot/app_icon1.png)
