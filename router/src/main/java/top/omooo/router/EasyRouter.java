package top.omooo.router;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

import top.omooo.logger.Logger;

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
                if (metaDataInfo == null) {
                    //ignore
                    //即使没写 meta_data，也不会返回 null
//                    Logger.e(TAG, META_DATE_EMPTY_DESC, activityInfo.name);
                } else {
                    bundle = metaDataInfo.metaData;
                    if (bundle == null || TextUtils.isEmpty(bundle.getString(PAGE_NAME))) {
                        Logger.e(TAG, PAGE_NAME_EMPTY_DESC, activityInfo.name);
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

    public EasyRouter with(Context context) {
        this.mContext = context;
        return this;
    }

    public EasyRouter putAll(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    public void navigate(String pageName) {
        if (TextUtils.isEmpty(pageName) || mRouterMap.get(pageName) == null) {
            Logger.e(TAG, PAGE_NAME_NOT_AVAIRABLE, "pageName: " + pageName);
            return;
        }
        Intent intent = new Intent(mContext, (Class<?>) mRouterMap.get(pageName));
        ActivityCompat.startActivity(mContext, intent, mBundle);
    }

    /**
     * 编译时注解扫描所有 pageName，然后返回路由表
     * {@link top.omooo.router_annotations.annotations.BindMetaDataAnn}
     */
    public void setRouterMap(HashMap<String, Object> map) {
        this.mRouterMap = map;
    }

    // TODO: 2019/3/21
    /**
     * 遗留问题:
     * 1. 即使没写 meta_data，也不会返回空
     */
}
