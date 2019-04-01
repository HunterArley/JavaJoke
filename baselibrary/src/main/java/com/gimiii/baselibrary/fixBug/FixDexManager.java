package com.gimiii.baselibrary.fixBug;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * 热修复类
 */
public class FixDexManager {
    private Context mContext;
    private File mDexDir;
    private final String TAG = "FixDexManager";

    public FixDexManager(Context context) {
        this.mContext = context;
        //获取应用可以访问的目录
        this.mDexDir = context.getDir("oDex", Context.MODE_PRIVATE);
    }

    //修复dex包
    public void fixDex(String fixDexPath) throws Exception {

        //2.获取下载好的补丁的DexElement
        //2.1移动到系统能够访问的dex目录下  ClassLoader
        File srcFile = new File(fixDexPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException(fixDexPath);
        }
        File targetFile = new File(mDexDir, srcFile.getName());

        if (targetFile.exists()) {
            Log.d(TAG, "patch [" + fixDexPath + "] has be loaded");
            return;
        }
        copyFile(srcFile, targetFile);
        //2.2ClassLoader读取fixDex路径   为什么加入到集合？
        // 已启动可能就要修复BaseApplication
        List<File> fixDexFiles = new ArrayList<>();
        fixDexFiles.add(targetFile);

        fixDexFiles(fixDexFiles);
    }

    /**
     * 把dexElements注入到classLoader中
     *
     * @param classLoader
     * @param dexElements
     */
    private void injectDexElements(ClassLoader classLoader, Object dexElements) throws Exception {
        //先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //获取PathList里面的DexElement
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        dexElementsField.set(pathList, dexElements);
    }

    private Object combineArray(Object arrayList, Object arrayRhs) {
        Class<?> localClass = arrayList.getClass().getComponentType();
        int i = Array.getLength(arrayList);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; k++) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayList, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }


    /**
     * 文件拷贝
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            if (!dest.exists()) {
                dest.createNewFile();
            }
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 从ClassLoader中获取DexElements
     *
     * @param classLoader
     * @return
     */
    private Object getDexElementByClassLoader(ClassLoader classLoader) throws Exception {
        //先获取pathList
        Field pathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathList = pathListField.get(classLoader);
        //获取PathList里面的DexElement
        Field dexElementsField = pathList.getClass().getDeclaredField("dexElements");
        dexElementsField.setAccessible(true);
        return dexElementsField.get(pathList);
    }

    /**
     * 加载全部的修复包
     */
    public void loadFixDex() throws Exception{
        File[] dexFiles = mDexDir.listFiles();

        List<File> fixDexFiles = new ArrayList<>();

        for (File dexFile : dexFiles) {
            if (dexFile.getName().endsWith(".dex")) {
                fixDexFiles.add(dexFile);
            }
        }
        fixDexFiles(fixDexFiles);
    }

    /**
     * 修复dex
     * @param fixDexFiles
     */
    private void fixDexFiles(List<File> fixDexFiles) throws Exception{
        //1.先获取已经运行的DexElement
        ClassLoader classLoader = mContext.getClassLoader();

        Object dexElements = getDexElementByClassLoader(classLoader);

        File optimizedDirectory = new File(mDexDir, "odex");
        if (!optimizedDirectory.exists()) {
            optimizedDirectory.mkdir();
        }

        //修复
        for (File fixDexFile : fixDexFiles) {
            /**
             * dexPath  dex路径
             * optimizedDirectory   解压路径
             * librarySearchPath  so路径
             */
            ClassLoader fixDexClassLoader = new BaseDexClassLoader(
                    fixDexFile.getAbsolutePath(),//dex路径  必须要在应用目录下的dex文件中
                    optimizedDirectory,//解压路径
                    null,
                    classLoader
            );

            Object fixDexElements = getDexElementByClassLoader(fixDexClassLoader);

            //3.把补丁的DexElement插到已经运行的DexElement的最前面  去合并
            //classLoader数组合并fixDexElements数组
            //3.1合并完成
            dexElements = combineArray(fixDexElements, dexElements);
        }

        //3.2把合并的数组注入到原来的类中classLoader
        injectDexElements(classLoader, dexElements);

    }
}
