package com.example.admin.testcoordinatorlayoutfresh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.testcoordinatorlayoutfresh.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:底部dialog
 * Data：2018/11/10-13:54
 * Author: ly
 * <p>
 * todo 添加头部
 */
public class QMUIBottomSheet extends Dialog {

    private static final String TAG = "QMUIBottomSheet";
    public final static String LIST = "1";
    public final static String GRID = "2";
    // 动画时长
    private final static int mAnimationDuration = 200;
    // 持有 ContentView，为了做动画
    private View mContentView;
    private boolean mIsAnimating = false;

    public QMUIBottomSheet(@NonNull Context context) {
        super(context, R.style.QMUI_BottomSheet);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        // 在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int screenWidth = getScreenWidth(getContext());
        params.width = screenWidth;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }


    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        mContentView = view;
        super.setContentView(view, params);
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setContentView(@NonNull View view) {
        mContentView = view;
        super.setContentView(view);
    }

    /**
     * BottomSheet升起动画
     */
    private void animateUp() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        );
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        mContentView.startAnimation(set);
    }

    /**
     * BottomSheet降下动画
     */
    private void animateDown() {
        if (mContentView == null) {
            return;
        }
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        );
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(translate);
        set.addAnimation(alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(mAnimationDuration);
        set.setFillAfter(true);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsAnimating = false;

                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        // java.lang.IllegalArgumentException: View=com.android.internal.policy.PhoneWindow$DecorView{22dbf5b V.E...... R......D 0,0-1080,1083} not attached to window manager
                        // 在dismiss的时候可能已经detach了，简单try-catch一下
                        try {
                            QMUIBottomSheet.super.dismiss();
                        } catch (Exception e) {
                            Log.w(TAG, "dismiss error\n" + Log.getStackTraceString(e));
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContentView.startAnimation(set);
    }

    @Override
    public void show() {
        super.show();
        animateUp();
//        if (mOnBottomSheetShowListener != null) {
//            mOnBottomSheetShowListener.onShow();
//        }
    }

    @Override
    public void dismiss() {
        if (mIsAnimating) {
            return;
        }
        animateDown();
    }


    //提供默认实现 用户也可以自己提供mLayoutManager 和 BaseQuickAdapter
    public static class BottomListSheetBuilder {

        private Context mContext;
        private QMUIBottomSheet mDialog;
        private String mTitle;
        private String mcancel;
        private OnDismissListener mOnBottomDialogDismissListener;
        private OnSheetItemClickListener mOnSheetItemClickListener;
        private RecyclerView.LayoutManager mLayoutManager;
        private RecyclerView mRecyclerView;
        private BaseQuickAdapter mAdapter; //可以不设置 用内置的defaultAdapter
        private View.OnClickListener onBottomClickListener;
        //是否显示标题和底部
        private boolean showTitle;
        private boolean showCancel;

        private TextView mTextTitle;
        private TextView mTextCancel;

        //recycleView加载的面板 列表和宫格

        private String item_type = "1";
        private int spanCount = 4 ;
        private List<BottomItem> mList;
        public interface OnSheetItemClickListener {
            void onClick(BaseQuickAdapter adapter, QMUIBottomSheet dialog, View itemView, int position);
        }

        public BottomListSheetBuilder(Context context) {
            mContext = context;
            mList = new ArrayList<>();
        }

        public BottomListSheetBuilder setOnBottomClickListener(View.OnClickListener onBottomClickListener) {
            this.onBottomClickListener = onBottomClickListener;
            return  this;
        }

        public BottomListSheetBuilder setItem_type(String item_type) {
            this.item_type = item_type;
            return this;
        }

        public BottomListSheetBuilder setList(List<BottomItem> list) {
            mList = list;
            return this ;
        }

        public BottomListSheetBuilder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return  this;
        }

        public BottomListSheetBuilder setOnSheetItemClickListener(OnSheetItemClickListener onSheetItemClickListener) {
            mOnSheetItemClickListener = onSheetItemClickListener;
            return this;
        }

        public BottomListSheetBuilder setOnBottomDialogDismissListener(OnDismissListener listener) {
            mOnBottomDialogDismissListener = listener;
            return this;
        }

        public BottomListSheetBuilder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public BottomListSheetBuilder setTitle(int resId) {
            mTitle = mContext.getResources().getString(resId);
            return this;
        }

        public BottomListSheetBuilder setCancelTitle(String title) {
            mcancel = title;
            return this;
        }

        public BottomListSheetBuilder setCancelTitle(int resId) {
            mcancel = mContext.getResources().getString(resId);
            return this;
        }

        public BottomListSheetBuilder setAdapter(BaseQuickAdapter adapter) {
            mAdapter = adapter;
            return this;
        }

        public BottomListSheetBuilder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
            return this;
        }

        public BottomListSheetBuilder setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public BottomListSheetBuilder setShowCancel(boolean showCancel) {
            this.showCancel = showCancel;
            return this;
        }
        public BottomListSheetBuilder addItem(int imageRes, String textAndTag) {
            mList.add(new BottomItem(textAndTag , imageRes));
            return this;
        }

        public BottomListSheetBuilder addItem(String text) {
            mList.add(new BottomItem(text));
            return this;
        }


        public QMUIBottomSheet build() {
            mDialog = new QMUIBottomSheet(mContext);
            View contentView = buildViews();
            mDialog.setContentView(contentView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (mOnBottomDialogDismissListener != null) {
                mDialog.setOnDismissListener(mOnBottomDialogDismissListener);
            }
            return mDialog;
        }

        private View buildViews() {
            View wrapperView = View.inflate(mContext, getContentViewLayoutId(), null);
            mTextTitle = wrapperView.findViewById(R.id.title);
            mTextCancel = wrapperView.findViewById(R.id.cancel);
            mRecyclerView = wrapperView.findViewById(R.id.recycle);

            //设置标题
            if (showTitle && !TextUtils.isEmpty(mTitle)) {
                mTextTitle.setVisibility(View.VISIBLE);
                mTextTitle.setText(mTitle);
            } else {
                mTextTitle.setVisibility(View.GONE);
            }
            //设置底部
            if (showCancel && !TextUtils.isEmpty(mcancel)) {
                mTextCancel.setVisibility(View.VISIBLE);
                mTextCancel.setText(mcancel);
            } else {
                mTextCancel.setVisibility(View.GONE);
            }

            mTextCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    if (onBottomClickListener!=null)
                        onBottomClickListener.onClick(v);
                }
            });
            if (mLayoutManager == null){
                if (item_type.equals(LIST)){
                    mLayoutManager = new LinearLayoutManager(mContext);
                }else {
                    mLayoutManager = new GridLayoutManager(mContext , spanCount);
                }
            }
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.setHasFixedSize(true);

            if (mAdapter == null){
                if (mList==null || mList.size()==0)
                    throw  new IllegalArgumentException("请设置数据源");
                mAdapter = new DefaultAdapter(mList);
            }
            mRecyclerView.setAdapter(mAdapter);

            if (needToScroll()) {
                mRecyclerView.getLayoutParams().height = getMaxDialogHeight();
            }
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (mOnSheetItemClickListener != null) {
                        mOnSheetItemClickListener.onClick(adapter, mDialog, view, position);
                    }
                }
            });
            return wrapperView;
        }

        private int getMaxDialogHeight() {
            int defaultHeight = DensityUtil.dp2px(500);
            if (showCancel)
                defaultHeight = defaultHeight - DensityUtil.dp2px(50);
            if (showTitle)
                defaultHeight = defaultHeight - DensityUtil.dp2px(50);
            return defaultHeight;
        }

        //适配长布局 让recycleView滚动
        private boolean needToScroll() {

            return mAdapter.getItemCount() > 8;
        }

        protected int getContentViewLayoutId() {
            return R.layout.bottom_sheet_list;
        }

        private class DefaultAdapter extends  BaseQuickAdapter<BottomItem , BaseViewHolder>{

            public DefaultAdapter(List<BottomItem> list) {
                super(item_type.equals(LIST) ? R.layout.item: R.layout.item_grid , list);
            }
            @Override
            protected void convert(BaseViewHolder helper, BottomItem item) {
                if (item_type.equals(LIST)){
                    helper.setText(R.id.text , item.item);
                }else {
                    helper.setText(R.id.grid_item_title , item.item);
                    helper.setImageResource(R.id.grid_item_image , item.imgRes);
                }
            }
        }
    }
}
