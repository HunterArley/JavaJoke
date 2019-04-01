package com.gimiii.myfirstapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gimiii.baselibrary.navigation.AbsNavigationBar;

public class DefaultNavigationBar<D extends AbsNavigationBar.Builder.AbsNavigationParams> extends AbsNavigationBar<DefaultNavigationBar.Builder.DefaultNavigationParams> {

    public DefaultNavigationBar(DefaultNavigationBar.Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayout() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        //绑定效果
        setText(R.id.title, getParams().mTitle);
        setText(R.id.rightText, getParams().mRightText);
        setOnClickListener(R.id.title, getParams().mRightClickListener);
    }

    public static class Builder extends AbsNavigationBar.Builder {
        private DefaultNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            P = new DefaultNavigationParams(context, parent);
        }

        @Override
        public DefaultNavigationBar<AbsNavigationParams> builder() {
            DefaultNavigationBar<AbsNavigationParams> navigationBar = new DefaultNavigationBar<AbsNavigationParams>(P);
            return navigationBar;
        }

        //设置所有效果
        public Builder setTitle(String title) {
            P.mTitle = title;
            return this;
        }

        public Builder setRight(String rightText) {
            P.mRightText = rightText;
            return this;
        }

        public Builder setLeft(String left) {
            P.mLeftText = left;
            return this;
        }

        public Builder setRightIcon(int rightRes) {
            P.rightIconRes = rightRes;
            return this;
        }

        public Builder setLeftIcon(int leftRes) {
            P.mLeftIconRes = leftRes;
            return this;
        }

        public Builder setTitleBackgroundColor(int bgColor) {
            P.bgColor = bgColor;
            return this;
        }

        public Builder setLeftOnClickListener(View.OnClickListener leftClickListener) {
            P.mLeftClickListner = leftClickListener;
            return this;
        }

        public Builder setRightClickListener(View.OnClickListener rightClickListener) {
            P.mRightClickListener = rightClickListener;
            return this;
        }

        public static class DefaultNavigationParams extends AbsNavigationParams {

            //标题
            public String mTitle;
            public String mRightText;
            //右边点击事件
            public View.OnClickListener mRightClickListener;
            public String mLeftText;
            //右边图片资源
            public int rightIconRes;
            //左边图片资源
            public int mLeftIconRes;
            public int bgColor;
            //左边点击事件
            public View.OnClickListener mLeftClickListner;

            //所有效果

            public DefaultNavigationParams(Context context, ViewGroup parent) {
                super(context, parent);
            }
        }
    }
}
