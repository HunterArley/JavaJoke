package com.gimiii.myfirstapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.gimiii.framelibrary.BaseSkinActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseSkinActivity {
    @BindView(R.id.btnSkip)
    Button mBtnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.btnSkip)
    public void onViewClicked() {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }
}
