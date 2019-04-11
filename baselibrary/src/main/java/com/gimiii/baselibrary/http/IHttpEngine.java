package com.gimiii.baselibrary.http;

import java.util.Map;

public interface IHttpEngine {

    //get请求
    void get(String url, Map<String,Object> params,EngineCallBack mCallBack);

    //Post请求
    void post(String url, Map<String,Object> params,EngineCallBack mCallBack);
}
