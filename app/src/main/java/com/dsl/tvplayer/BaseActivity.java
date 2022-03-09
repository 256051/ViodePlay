package com.dsl.tvplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity{


    protected BaseActivity baseActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        baseActivity = this;
        initView();
    }

    protected abstract void initView();

    protected abstract int getLayoutId();
}
