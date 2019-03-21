package top.omooo.easyrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import top.omooo.router.EasyRouter;
import top.omooo.router_annotations.annotations.BindMetaDataAnn;

@BindMetaDataAnn("main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_jump_to_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyRouter.getInstance().with(MainActivity.this).navigate("second");
            }
        });
        findViewById(R.id.tv_jump_to_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyRouter.getInstance().with(MainActivity.this).navigate("third");
            }
        });
    }
}
