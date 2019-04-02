package com.gimiii.baselibrary.navigation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 头部的Builder基类
 */
public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {
    private P mParams;
    private View mNavigationView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    protected String getString(int id) {
        return this.mParams.mContext.getResources().getString(id);
    }

    protected void setImageResource(int viewId, int resourceId) {
        ImageView imageView = findViewById(viewId);
        if (imageView != null) {
            imageView.setImageResource(resourceId);
        }
    }

    protected void setBackgroundColor(int viewId, int color) {
        View view = findViewById(viewId);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    protected void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public <T extends View> T findViewById(int viewID) {
        return (T) mNavigationView.findViewById(viewID);
    }

    protected void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }

    //绑定和创建View
    private void createAndBindView() {
        if (mParams.mParent == null) {
            ViewGroup activityRoot = (ViewGroup) ((Activity) (mParams.mContext))
                    .getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }
        if (mParams.mParent == null) {
            return;
        }
        //1.创建View
        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayout(), mParams.mParent, false);
        //2.添加
        mParams.mParent.addView(mNavigationView, 0);
        applyView();
    }

    public abstract static class Builder {

        public abstract AbsNavigationBar builder();

        public static class AbsNavigationParams {
            public Context mContext;
            public ViewGroup mParent;

            public AbsNavigationParams(Context context, ViewGroup parent) {
                this.mContext = context;
                this.mParent = parent;
            }
        }
    }
}
