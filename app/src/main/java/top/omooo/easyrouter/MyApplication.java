package top.omooo.easyrouter;

import android.app.Application;

import top.omooo.router.EasyRouter;

/**
 * Created by Omooo
 * Date:2019/3/20
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.getInstance().inject(this);
    }
}
