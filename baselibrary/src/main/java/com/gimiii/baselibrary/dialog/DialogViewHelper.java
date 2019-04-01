package com.gimiii.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

class DialogViewHelper {
    private View mContentView = null;

    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context context, int viewLayoutResId) {
        this();
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param text
     */
    public void setText(int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<View>(view));
            }
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = mContentView.findViewById(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
