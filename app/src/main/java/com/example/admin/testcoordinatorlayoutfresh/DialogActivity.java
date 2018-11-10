package com.example.admin.testcoordinatorlayoutfresh;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.testcoordinatorlayoutfresh.dialog.BottomItem;
import com.example.admin.testcoordinatorlayoutfresh.dialog.QMUIBottomSheet;

import java.util.Arrays;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void show(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .setCancelTitle("取消")
                .setShowCancel(false)
                .setTitle("删除提示")
                .setShowTitle(false)
                .setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item, Arrays.asList("item1", "item2", "item3", "item4", "item5",
                        "item6","item7", "item8", "item9", "item10","item11", "item12", "item13", "item14")) {
                    @Override
                    protected void convert(BaseViewHolder helper, String item) {
                        helper.setText(R.id.text , item);
                    }
                })
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BaseQuickAdapter adapter ,QMUIBottomSheet dialog, View itemView, int position) {
                        dialog.dismiss();
                        Log.d("--" , adapter.getItem(position).toString());
                }})
                .build()
                .show();
    }
    public void show1(View view) {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .setItem_type(QMUIBottomSheet.LIST)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BaseQuickAdapter adapter, QMUIBottomSheet dialog, View itemView, int position) {
                        dialog.dismiss();
                        Log.d("--" ,((BottomItem) adapter.getItem(position)).item);
                    }
                })
                .addItem("item1")
                .addItem("item2")
                .addItem("item3")
                .addItem("item4")
                .addItem("item5")
                .addItem("item6")
                .addItem("item7")
                .addItem("item8")
                .build()
                .show();
    }

    public void show2(View view) {

        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .setItem_type(QMUIBottomSheet.GRID)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BaseQuickAdapter adapter, QMUIBottomSheet dialog, View itemView, int position) {
                        dialog.dismiss();
                        Log.d("--" ,((BottomItem) adapter.getItem(position)).item);
                    }
                })
                .addItem(R.mipmap.icon_more_operation_save , "保存")
                .addItem(R.mipmap.icon_more_operation_share_chat , "微信")
                .addItem(R.mipmap.icon_more_operation_share_friend , "朋友圈")
                .addItem(R.mipmap.icon_more_operation_share_moment , "评论")
                .addItem(R.mipmap.icon_more_operation_share_weibo , "微博")
                .setShowCancel(true)
                .setCancelTitle("分享")
                .build()
                .show();
    }
}
