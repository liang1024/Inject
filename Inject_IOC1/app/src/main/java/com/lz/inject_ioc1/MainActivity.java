package com.lz.inject_ioc1;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lz.inject_ioc1.ioc.ViewInjectUtils;
import com.lz.inject_ioc1.ioc.annotation.ContentView;
import com.lz.inject_ioc1.ioc.annotation.ViewInject;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.onclick1)
    Button mButton1;
    @ViewInject(R.id.onclick2)
    Button mButton2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);
        ViewInjectUtils.inject(this);


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "我是按钮1", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "我是按钮1", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "我是按钮2", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "我是按钮2", Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });
    }
}
