package com.gimiii.baselibrary.http;

public interface EngineCallBack {
    public void onError(Exception mE);

    public void onSuccess(String result);

    //默认的
    public final EngineCallBack DEFUALT_CALL_BACK=new EngineCallBack() {
        @Override
        public void onError(Exception mE) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
