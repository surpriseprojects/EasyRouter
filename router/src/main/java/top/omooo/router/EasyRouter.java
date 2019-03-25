package top.omooo.router;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import top.omooo.logger.Logger;
import top.omooo.logger.StackTraceUtil;
import top.omooo.router_annotations.annotations.Router;

/**
 * Created by Omooo
 * Date:2019/3/20
 */
public class EasyRouter {

    private static final String TAG = "EasyRouter";

    private static final String PAGE_NAME = "pageName";
    //路由表 key:pageName value: Activity
    private HashMap<String, Object> mRouterMap;
    private Context mContext;
    private Bundle mBundle;

    private boolean isUseAnno = true;  //使用注解

    private static final String META_DATE_EMPTY_DESC = "This Activity doesn't have metaData!";
    private static final String PAGE_NAME_EMPTY_DESC = "This MetaData doesn't have pageName!";
    private static final String PAGE_NAME_NOT_AVAIRABLE = "This pageName is not available!";

    public static EasyRouter getInstance() {
        return EasyRouterHolder.sRouter;
    }

    private static class EasyRouterHolder {
        private static EasyRouter sRouter = new EasyRouter();
    }

    /**
     * 1. 通过 Application 获得所有 ActivityInfo
     * 2. 通过 ActivityInfo 获得每一个 Activity 的 MetaData 元数据
     * 注意: 获取元数据也要构造一个新的 ActivityInfo
     */
    public void inject(Application application) {
        mRouterMap = new HashMap<>();
        if (isUseAnno) {
            getRouterMapFromAnno();
            return;
        }
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
                        mRouterMap.put(bundle.getString(PAGE_NAME), Class.forName(activityInfo.name));
                        Logger.d(TAG, null, "ClassName: " + activityInfo.name,
                                "PageName: " + bundle.getString(PAGE_NAME));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
            //ignore
            e.printStackTrace();
        }
    }

    public EasyRouter with(@NonNull Context context) {
        this.mContext = context;
        return this;
    }

    public void navigate(String pageName) {
        Log.i(TAG, "navigate: ");
        if (TextUtils.isEmpty(pageName) || mRouterMap.get(pageName) == null) {
            Logger.e(TAG, PAGE_NAME_NOT_AVAIRABLE, StackTraceUtil.getStackTrace(), "pageName: " + pageName);
            return;
        }
        Intent intent = new Intent(mContext, (Class<?>) mRouterMap.get(pageName));
        if (mBundle != null){
            intent.putExtras(mBundle);
        }
        mContext.startActivity(intent);
    }

    /**
     * 编译时注解扫描所有 pageName，然后返回路由表
     * {@link Router}
     */
    public void getRouterMapFromAnno() {
        try {
            Class clazz = Class.forName("top.omooo.easyrouter.RouterFactory");
            Method method = clazz.getMethod("init");
            method.invoke(null);
            mRouterMap = (HashMap<String, Object>) clazz.getField("sHashMap").get(clazz);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加 Bundle 数据
     */

    public EasyRouter putAll(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    public EasyRouter putString(String key, String value) {
        Log.i(TAG, "putString: ");
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mBundle.putString(key, value);
        }
        return this;
    }

    public EasyRouter putInt(String key, int value) {
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mBundle.putInt(key, value);
        }
        return this;
    }

    public EasyRouter putParcelable(String key, Parcelable value) {
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mBundle.putParcelable(key, value);
        }
        return this;
    }

    public EasyRouter putSerializable(String key, Serializable value) {
        if (mBundle == null) {
            mBundle = new Bundle();
        } else {
            mBundle.putSerializable(key, value);
        }
        return this;
    }
}
