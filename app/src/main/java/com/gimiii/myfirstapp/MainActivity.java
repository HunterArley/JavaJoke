package com.gimiii.myfirstapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gimiii.baselibrary.ExceptionCrashHandler;
import com.gimiii.baselibrary.fixBug.FixDexManager;
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
        fixDexBug();
    }

    /**
     * 修复文件
     */
    private void fixDexBug() {
        //每次启动的时候，去后台获取差分包fix.apatch，然后修复本地的BUG
        //测试，直接获取本地内存卡中的fix.apatch
        File file = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (file.exists()) {
            //修复BUG
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(file.getAbsolutePath());
                Toast.makeText(this, "修复成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "修复失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, null)
                .setTitle("我是title")
                .setRight("发布")
                .setLeftIcon(R.mipmap.back_icon)
                .setTitleBackgroundColor(R.color.black)
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击发布没成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .builder();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.btnSkip)
    public void onViewClicked() {
        showDialog();
    }

    private void showDialog() {

    }
}
