package com.juan.sensors.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by tinashe on 2017/08/31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getContentResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResourceId());
        ButterKnife.bind(this);
    }
}
