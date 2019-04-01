package com.gimiii.myfirstapp;

import android.app.Application;
import com.alipay.euler.andfix.patch.PatchManager;
import com.gimiii.baselibrary.ExceptionCrashHandler;
import com.gimiii.baselibrary.fixBug.FixDexManager;

public class BaseApplication extends Application {
    public static PatchManager sPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        //设置全局异常捕捉类
        ExceptionCrashHandler.getExceptionCrashHandler().init(this);

//        //初始化阿里的热修复
//        sPatchManager = new PatchManager(this);
//
//        //初始化版本，获取当前应用的版本
//        sPatchManager.init("1.0");
//
//        //加载之前的包
//        sPatchManager.loadPatch();
        try {
            FixDexManager fixDexManager = new FixDexManager(this);
            //加载所有修复的dex包
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
