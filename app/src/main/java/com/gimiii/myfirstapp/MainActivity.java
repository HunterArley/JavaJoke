package com.gimiii.myfirstapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.gimiii.baselibrary.ExceptionCrashHandler;
import com.gimiii.framelibrary.BaseSkinActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseSkinActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.btnSkip)
    Button mBtnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

        //获取上次的崩溃信息上传到服务器
        File crashFile = ExceptionCrashHandler.getExceptionCrashHandler().getCrashFile();
        if (crashFile.exists()) {
            //上传到服务器
            Log.e(TAG, "上传异常文件");
        }

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
        int i = 2 / 0;
    }
}
