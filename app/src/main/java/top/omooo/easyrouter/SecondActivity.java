package top.omooo.easyrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import top.omooo.router.EasyRouter;

/**
 * Created by Omooo
 * Date:2019/3/20
 */
public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.tv_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyRouter.getInstance().with(SecondActivity.this).navigate("third");
            }
        });
    }
}
