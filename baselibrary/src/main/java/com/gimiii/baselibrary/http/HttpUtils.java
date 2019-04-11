package com.gimiii.baselibrary.http;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import java.util.Map;

public class HttpUtils {

    private String mUrl;
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0012;

    private Map<String, Object> mParams;

    //上下文
    private Context mContext;

    public HttpUtils(Context context) {
        mContext = context;
        mParams = new ArrayMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public static String jointParams(String mUrl, Map<String, Object> mParams) {
        return null;
    }

    public HttpUtils url(String url) {
        mUrl = url;
        return this;
    }

    //请求方式
    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    //添加参数
    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }

    //添加回调
    public void execute(EngineCallBack mCallBack) {
        if (mCallBack == null) {
            mCallBack=EngineCallBack.DEFUALT_CALL_BACK;
        }

        //判断执行方法
        if (mType == POST_TYPE) {
            post(mUrl,mParams,mCallBack);
        }

        if (mType == GET_TYPE) {
            get(mUrl, mParams, mCallBack);
        }

    }

    public void execute() {
        execute(null);
    }

    private static IHttpEngine mIHttpEngine = new OkhttpEngine();

    //Application初始化引擎
    public static void init(IHttpEngine httpEngine) {
        mIHttpEngine = httpEngine;
    }

    /**
     * 每次可以自带引擎
     *
     * @param httpEngine
     */
    public void exchangeEngine(IHttpEngine httpEngine) {
        mIHttpEngine = httpEngine;
    }

    private void get(String url, Map<String, Object> params, EngineCallBack mCallBack) {
        mIHttpEngine.get(url, params, mCallBack);
    }

    private void post(String url, Map<String, Object> params, EngineCallBack mCallBack) {
        mIHttpEngine.post(url, params, mCallBack);
    }
}
