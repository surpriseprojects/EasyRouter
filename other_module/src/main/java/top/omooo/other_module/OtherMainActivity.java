package top.omooo.other_module;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import top.omooo.router_annotations.annotations.Router;

/**
 * 模拟多模块跳转
 */
@Router("otherMain")
public class OtherMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_main);
        Toast.makeText(this, "Other Modlue Activity", Toast.LENGTH_SHORT).show();
    }
}
