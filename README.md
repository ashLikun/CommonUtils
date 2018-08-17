[![Release](https://jitpack.io/v/ashLikun/CommonUtils.svg)](https://jitpack.io/#ashLikun/CommonUtils)

CustomMvp项目简介
    项目Mvp框架
    基类activity和fragment
## 使用方法

build.gradle文件中添加:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
并且:

```gradle
dependencies {
    implementation 'com.github.ashLikun:CommonUtils:{latest version}'
}
```

## 详细介绍
utils是一系列通用类、辅助类、工具类的集合

其中包括bitmap处理，文件操作，加密存储器，shell命令，计数器，均值器，吐司，日志，校验，提示，网络监测等基础功能，以及一些Base64、MD5、Hex、Byte、Number、Dialog、Filed、Class、Package、Telephone、Random等工具类。

1:动画相关
2:简单的
    Averager:计算平均数
    FlashLight:手电筒
    NetworkUtils:网络环境
    TimeAverager:计数器
    TimeCounter:统计计数
    WakeLock:屏幕管理， 点亮、关闭屏幕，判断屏幕是否点亮
3:加密相关encryption
4:http相关
5:其他工具
6:多进程Sp
7:广播receiver
8:UI
