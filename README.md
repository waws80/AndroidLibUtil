# AndroidLibUtil

### **一个安卓开发工具库**

[![](https://jitpack.io/v/waws80/AndroidLibUtil.svg)](https://jitpack.io/#waws80/AndroidLibUtil)
![GitHub repo size](https://img.shields.io/github/repo-size/waws80/AndroidLibUtil.svg)
![GitHub language count](https://img.shields.io/github/languages/count/waws80/AndroidLibUtil.svg)
![GitHub](https://img.shields.io/github/license/waws80/AndroidLibUtil.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/waws80/AndroidLibUtil.svg)
[![](https://img.shields.io/badge/Android-Util-blue.svg)]()

## **0、版本**

### **1.0.2版本**
* 扩展Gson工具类


### **1.0.0版本**
* 基本功能的实现
* 网络工具类封装完成

## **1、引用**
* Gradle中依赖

    ```
    implementation 'com.github.waws80:AndroidLibUtil:1.0.2'
    ```
* 如果出现core库冲突

    ```
    implementation (rootProject.ext.androidLibUtils){
        exclude group :'androidx.core' , module: 'core-ktx'
    }
    ```
    

## **2、初始化**
* 初始化
    
    ```
    //初始化工具类
    LibUtils.install(application: Application, debug: Boolean)
    ```
* 注销

    ```
    //注销
    LibUtils.destroy()
    ```
## **3、使用**

**① 基本工具类的使用**

* 项目信息
    ```
    LibUtils.appInfo()
    ```
* Base64工具类
    ```
    LibUtils.base64Util()
    ```
* BitMap工具类
    ```
    LibUtils.bitmapUtil()
    ```
* 缓存工具类
    ```
    LibUtils.cacheUtil()
    ```
* 剪切板值的设置和获取
    ```
    LibUtils.clipBoardUtil()
    ```
* html转换Spanned
    ```
    LibUtils.fromHtml("<span>a<span>", imageGetter)
    ```
* 全局异常捕获处理
    ```
    LibUtils.crashHandle()
    ```
* 日期工具类
    ```
    LibUtils.dateUtil()
    ```
* 文件工具类
    ```
    LibUtils.fileUtil()
    ```
* Gson工具类
    ```
    LibUtils.gsonUtil()
    ```
* 键盘工具类
    ```
    LibUtils.keyBoardUtil()
    ```
* 日志工具类
    ```
    LibUtils.logUtil()
    ```
* MD5工具类（32位小写）
    ```
    LibUtils.md5Util()
    ```
* MMKV工具类
    ```
    LibUtils.mmkvUtil()
    ```
* 网络工具类
    ```
    LibUtils.netWorkUtil()
    ```
* 正则工具类
    ```
    LibUtils.regexUtil()
    ```
* 厂商系统工具类
    ```
    LibUtils.romUtil()
    ```
* 屏幕工具类
    ```
    LibUtils.screenUtil()
    ```
* SDCard工具类
    ```
    LibUtils.sdCardUtil()
    ```
* 状态栏工具类
    ```
    LibUtils.statusBarUtil()
    ```
* 定时器工具类
    ```
    LibUtils.timer()
    ```
* Toast工具类
    ```
    LibUtils.toast()
    ```
* 不会内存溢出的Handler
    ```
    LibUtils.weakHandler()
    ```
* WebView工具类
    ```
    LibUtils.webViewDelegate()
    ```
**② 网络工具类的高级使用**

<font color='#FF4321'>注意：</font> 网络工具类中的注解目前只在Activity中生效。(自动注册和注销开发者只需要书写自己的接收方法即可)

* 接收所有网络状态
    ```
    @SubscribeNetWork(ThreadType.BACKGROUND)
    fun onNetChange(type: NetWorkType){}
    ```
* 接收WIFI网络状态
    ```
    @SubscribeNetWork(ThreadType.MAIN,receiveType = NetWorkType.WIFI)
    fun receiverWifi(type: NetWorkType){}
    ```
* 接收MOBILE网络状态
    ```
    @SubscribeNetWork(receiveType = NetWorkType.MOBILE)
    fun receiverMobile(type: NetWorkType){}
    ```
# **License**
* <br>

    ```
    Copyright [2019] The AndroidLibUtil Author

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    ```
    
