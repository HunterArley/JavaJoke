package com.gimiii.baselibrary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常捕捉类
 * 单例设计模式实现
 */
public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "ExceptionCrashHandler";
    private static ExceptionCrashHandler sExceptionCrashHandler;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    public static ExceptionCrashHandler getExceptionCrashHandler() {
        if (sExceptionCrashHandler == null) {
            //解决多并发的问题
            synchronized (ExceptionCrashHandler.class) {
                if (sExceptionCrashHandler == null) {
                    sExceptionCrashHandler = new ExceptionCrashHandler();
                }
            }
        }
        return sExceptionCrashHandler;
    }

    //获取应用信息
    private Context mContext;

    public void init(Context context) {
        this.mContext = context;
        //设置全局的异常类为本类（this）
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mUncaughtExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //全局异常
        Log.e(TAG, "报异常了");
        //写入本地文件  e  当前的版本  手机信息
        //1.崩溃的详细信息
        //2.应用信息  包名  Ban本号
        //3.手机信息
        //4.保存当前文件，等应用再次启动再上传（上传的问题，上传文件不在这里处理）
        String crashFileName = saveInfoToSD(e);
        Log.e(TAG, crashFileName);
        cacheCrashFile(crashFileName);

        //系统默认的处理
        mUncaughtExceptionHandler.uncaughtException(t, e);
    }

    //保存异常信息
    private void cacheCrashFile(String crashFileName) {
        SharedPreferences sp = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE);
        sp.edit().putString("CRASH_FILE_NAME", crashFileName).apply();
    }

    /**
     * 保存获取的软件信息，设备信息和出错信息保存在SDCard中
     *
     * @param ex
     * @return
     */
    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        //1.手机信息+应用信息
        for (Map.Entry<String, String> entry : obtainSimpleInfo(mContext).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        //2. 异常信息
        sb.append(obtainExceptionInfo(ex));
        // 保存文件  手机的目录
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(mContext.getFilesDir() + File.separator + "crash" + File.separator);

            //先删除之前的异常信息
            if (dir.exists()) {
                //删除该目录下的所有子文件
                deleteDir(dir);
            }

            //再重新创建文件夹
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                fileName = dir.toString() + File.separator + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

    //获取异常信息文件
    public File getCrashFile() {
        String crashFileName = mContext.getSharedPreferences("crash", Context.MODE_PRIVATE).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }

    //格式化时间
    private String getAssignTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat(time);
        long currentTime = System.currentTimeMillis();
        return dateFormat.format(currentTime);
    }

    //删除文件
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (File child : children) {
                child.delete();
            }
        }
        return true;
    }

    /**
     * 获取系统未捕捉的异常信息
     *
     * @param ex
     * @return
     */
    private String obtainExceptionInfo(Throwable ex) {
        //Java基础  异常
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", packageInfo.versionName);
        map.put("versionCode", "" + packageInfo.versionCode);
        map.put("MODEL", Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", "" + getMobileInfo());
        return map;
    }

    /**
     * 获取手机信息
     *
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            //利用反射获取Build类的所有属性
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = null;
                value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
