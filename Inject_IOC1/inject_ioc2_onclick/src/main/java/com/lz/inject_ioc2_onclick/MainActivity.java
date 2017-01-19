package com.lz.inject_ioc2_onclick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lz.inject_ioc2_onclick.ioc.ViewInjectUtils;
import com.lz.inject_ioc2_onclick.ioc.annotation.ContentView;
import com.lz.inject_ioc2_onclick.ioc.annotation.OnClick;
import com.lz.inject_ioc2_onclick.ioc.annotation.ViewInject;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.button1)
    private Button mButton1;
    @ViewInject(R.id.button2)
    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);
        ViewInjectUtils.inject(this);
    }

    /**
     * 注解点击事件
     *
     * @param view
     */
    @OnClick({R.id.button1, R.id.button2})
    public void initClickBtnInvoKed(View view) {
        //        Log.e("TAG", "点击了");
        switch (view.getId()) {
            case R.id.button1:
                Toast.makeText(this, "点击了按钮1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Toast.makeText(this, "点击了按钮2", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
