package com.gimiii.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

class AlertController {
    private Dialog mDialog;
    private Window mWindow;
    private DialogViewHelper mHelper;


    public AlertController(Dialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    public void setWindow(Window window) {
        mWindow = window;
    }

    public void setText(int viewId, CharSequence text) {
        mHelper.setText(viewId, text);
    }

    public <T extends View> T getView(int viewId) {
        return mHelper.getView(viewId);
    }

    public void setHelper(DialogViewHelper helper) {
        mHelper = helper;
    }

    /**
     * 设置点击事件
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        mHelper.setOnClickListener(viewId, listener);
    }

    public static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        //点击空白是否能够取消
        public boolean mCancelable = true;
        //dialog取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //dialog消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //dialog Key监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //显式的布局
        public View mView;
        //布局id
        public int mViewLayoutResId;
        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //动画
        public int mAnimations = 0;
        //位置
        public int mGravity = Gravity.CENTER;
        //高度
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        //绑定和设置参数
        public void apply(AlertController alert) {
            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            alert.getDialog().setContentView(viewHelper.getContentView());

            //设置辅助类
            alert.setHelper(viewHelper);

            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                alert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                alert.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            Window window = alert.getWindow();

            window.setGravity(mGravity);
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }

}
