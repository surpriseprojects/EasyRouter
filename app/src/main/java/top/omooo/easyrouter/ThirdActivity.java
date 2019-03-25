package top.omooo.easyrouter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import top.omooo.router_annotations.annotations.Router;

/**
 * Created by Omooo
 * Date:2019/3/20
 */

@Router("third")
public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Toast.makeText(this, "传递的数据: " + getIntent().getStringExtra("name")
                , Toast.LENGTH_SHORT).show();
    }
}
