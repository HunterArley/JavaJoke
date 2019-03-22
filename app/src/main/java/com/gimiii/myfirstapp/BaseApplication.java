package com.gimiii.myfirstapp;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.gimiii.baselibrary.ExceptionCrashHandler;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //设置全局异常捕捉类
        ExceptionCrashHandler.getExceptionCrashHandler().init(this);
    }

}
