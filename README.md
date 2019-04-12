---
EasyRouter
---

#### 前言

路由框架作为模块化、组件化的基础组件，重要性不言而喻。业界也有很成熟的开源项目了，比如阿里的 ARouter，美团的 WRouter 等。

如果让你写，你会怎么做呢？写一个成熟的路由框架并不简单，永远不要以为自己看过源码了解其原理就掌握了，自己动手写的时候才会发现各种棘手问题。

本着造轮子的态度，按自己的想法去实现一个简单的路由框架，那该如何做呢？

#### 实现方式

实现原理是一致的，都是生成 key-Activity 的映射表，两种方式各有优劣，代码都不多，放心 fork。

##### 元数据形式

通过在注册 Activity 的 AndroidManifest 文件中添加元数据，形式如下：

```xml
        <activity android:name=".SecondActivity">
            <meta-data
                android:name="pageName"
                android:value="second"/>
        </activity>
        <activity android:name=".ThirdActivity" >
            <meta-data
                android:name="pageName"
                android:value="third"/>
        </activity>
```

在 Application 初始化的时候去遍历生成映射，核心代码如下（没错，就这么点）：

```java
    /**
     * 1. 通过 Application 获得所有 ActivityInfo
     * 2. 通过 ActivityInfo 获得每一个 Activity 的 MetaData 元数据
     * 注意: 获取元数据也要构造一个新的 ActivityInfo
     */
    public void inject(Application application) {
        mRouterMap = new HashMap<>();
        //使用 Meta-Data
        try {
            ActivityInfo[] activityInfos = application.getPackageManager()
                    .getPackageInfo(application.getPackageName(), PackageManager.GET_ACTIVITIES)
                    .activities;
            Bundle bundle;
            ActivityInfo metaDataInfo;
            for (ActivityInfo activityInfo : activityInfos) {
                metaDataInfo = application.
                        getPackageManager().
                        getActivityInfo(new ComponentName(application.getPackageName(), activityInfo.name), PackageManager.GET_META_DATA);
                if (metaDataInfo.metaData == null) {
                    Logger.e(TAG, META_DATE_EMPTY_DESC, StackTraceUtil.getStackTrace(), activityInfo.name);
                } else {
                    bundle = metaDataInfo.metaData;
                    if (TextUtils.isEmpty(bundle.getString(PAGE_NAME))) {
                        Logger.e(TAG, PAGE_NAME_EMPTY_DESC, StackTraceUtil.getStackTrace(), activityInfo.name);
                    } else {
                        mRouterMap.put(bundle.getString(PAGE_NAME), activityInfo.name);
                        Logger.d(TAG, null, "ClassName: " + activityInfo.name,
                                "PageName: " + bundle.getString(PAGE_NAME));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            //ignore
            e.printStackTrace();
        }
    }
```

##### 编译时注解

通过元数据的形式，在 Application 初始化的时候需要遍历所有的 Activity 拿到 ActivityInfo，笔者测试了一下，遍历 200+ 个 Activity 生成映射耗费了 500ms，对于极致的性能要求肯定是不希望出现的。那我们可以怎么进行优化呢？

可以通过编译时注解 + JavaPoet 的形式，和 ButterKnife 原理一样，就不多说了～

#### ToDo

1. 拦截器
2. 深入 ARouter 源码
