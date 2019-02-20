package com.example.admin.testcoordinatorlayoutfresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbarCommon;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewPager mViewPager ;
    private TabLayout mTabLayout;
    private MyPagerAdapter mMyPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbarCommon = findViewById(R.id.toolbar);
//        mCollapsingToolbarLayout = findViewById(R.id.coll);
//        mCollapsingToolbarLayout.setTitle("我都");
//        setTitlePadding();
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tab_net_music);
        setViewpager();
    }

    private void setViewpager() {

        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMyPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    private void setTitlePadding() {
        mToolbarCommon.getLayoutParams().height = ScreenUtils.getActionBarHeight(this)
//                + ScreenUtils.getStatusHeight(this)
                + dp2px(this, 45);
        mToolbarCommon.setPadding(0, 0,
                0, dp2px(this, 45));
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    public class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ChildFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "测试"+position;
        }
    }
}
