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
                metaDataInfo = application.getPackageManager().getActivityInfo(new ComponentName(application.getPackageName(), activityInfo.name), PackageManager.GET_META_DATA);
                if (metaDataInfo == null) {
                    Log.e(TAG, "Activity: " + activityInfo.name + " don't have metaData!");
                } else {
                    bundle = metaDataInfo.metaData;
                    if (bundle == null || TextUtils.isEmpty(bundle.getString(PAGE_NAME))) {
                        Log.e(TAG, "Activity: " + activityInfo.name + " don't have pageName!");
                    } else {
                        mRouterMap.put(bundle.getString(PAGE_NAME), Class.forName(activityInfo.name));
                        Log.i(TAG, "PageName: " + bundle.getString(PAGE_NAME));
                        Log.i(TAG, "ClassName: " + activityInfo.name);
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
            Log.e(TAG, "PageName: " + pageName + " is not available!");
            return;
        }
        Intent intent = new Intent(mContext, (Class<?>) mRouterMap.get(pageName));
        ActivityCompat.startActivity(mContext, intent, mBundle);
    }
}
