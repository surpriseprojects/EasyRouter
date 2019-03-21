package top.omooo.easyrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import top.omooo.router_annotations.annotations.BindMetaDataAnn;

/**
 * Created by Omooo
 * Date:2019/3/20
 */
@BindMetaDataAnn("second")
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
