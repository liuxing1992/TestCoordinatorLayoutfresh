package com.example.admin.testcoordinatorlayoutfresh;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * Description:
 * Data：2018/9/14-14:56
 * Author: ly
 */
public class ChildFragment extends Fragment implements OnRefreshListener {

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_child , container ,false);
        mRecyclerView = view.findViewById(R.id.recy);
        mRefreshLayout = view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity() ,1 ));
        mRecyclerView.setAdapter(mAdapter);
        for (int i = 0; i < 15; i++) {
            mAdapter.addData("item"+i);
        }
        return view;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.finishRefresh();
            }
        },1000);
    }

    public class MyAdapter extends BaseQuickAdapter<String , BaseViewHolder>{

        public MyAdapter() {
            super(R.layout.item);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {

            helper.setText(R.id.text , item) ;
        }
    }
}
