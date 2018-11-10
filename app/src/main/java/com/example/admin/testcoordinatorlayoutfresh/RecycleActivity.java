package com.example.admin.testcoordinatorlayoutfresh;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.admin.testcoordinatorlayoutfresh.bean.BookBean;
import com.example.admin.testcoordinatorlayoutfresh.bean.HomeBox;
import com.example.admin.testcoordinatorlayoutfresh.bean.ItemBean;
import com.example.admin.testcoordinatorlayoutfresh.view.ExpandableTextView2;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class RecycleActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView ;

    private MyAdapter mMyAdapter ;

    private HomeBox mHomeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);
        mRecyclerView = findViewById(R.id.recycle);
        mRecyclerView.setNestedScrollingEnabled(false);

        //读取json
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open("book.json");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String result =  newstringBuilder .toString();

        Gson gson = new Gson();
        mHomeBox = gson.fromJson(result, HomeBox.class);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyAdapter(R.layout.item_recy , R.layout.item_header , null);
        mRecyclerView.setAdapter(mMyAdapter);


        //设置数据源
        for (int i = 0; i < mHomeBox.getItemList().size(); i++) {
            MySection section = new MySection(true , mHomeBox.getItemList().get(i).getTitle());
            MySection section1 = new MySection(mHomeBox.getItemList().get(i));
            mMyAdapter.addData(section);
            mMyAdapter.addData(section1);
        }

    }

    public class  MyAdapter extends BaseSectionQuickAdapter<MySection , BaseViewHolder> implements ExpandableTextView2.OnExpandListener {

        private RecyclerView.RecycledViewPool mRecycledViewPool;
        private SparseArray<Integer> mPositionsAndStates = new SparseArray<>();
        public MyAdapter(int layoutResId, int sectionHeadResId, List<MySection> data) {
            super(layoutResId, sectionHeadResId, data);
            mRecycledViewPool = new RecyclerView.RecycledViewPool();
        }

        @Override
        protected void convertHead(BaseViewHolder helper, MySection item) {

            helper.setText(R.id.tv_title , item.header);
        }

        private int etvWidth;
        @Override
        protected void convert(BaseViewHolder helper, MySection item) {


           final ExpandableTextView2 textView = helper.getView(R.id.expand_text_view);

            RecyclerView view = helper.getView(R.id.re);
            view.setNestedScrollingEnabled(false);
            view.setRecycledViewPool(mRecycledViewPool);
            view.setLayoutManager(new LinearLayoutManager(RecycleActivity.this , LinearLayoutManager.HORIZONTAL ,false));
            Myadapter2 mMyadapter2  = new Myadapter2(R.layout.item_bg , null);

            if(etvWidth == 0){
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        etvWidth = textView.getWidth();
                    }
                });
            }

            textView.setTag(helper.getLayoutPosition());
            textView.setExpandListener(this);
            Integer state = mPositionsAndStates.get(helper.getLayoutPosition());

            textView.updateForRecyclerView("氨基酸瘋狂噶價格可展开和收起的TextView，就像GooglePlay里面显示应用的描述那样。可展开和收起的TextView，就像GooglePlay里面显示应用的描述那样。可展开和收起的TextView，就像GooglePlay里面显示应用的描述那样。"
                    , etvWidth, state== null ? 0 : state);//第一次getview时肯定为etvWidth为0


            view.setAdapter(mMyadapter2);
            ItemBean bookBean = item.t;
            mMyadapter2.setNewData(bookBean.getBooks());
        }

        @Override
        public void onExpand(ExpandableTextView2 view) {
            Object obj = view.getTag();
            if(obj != null && obj instanceof Integer){
                mPositionsAndStates.put((Integer)obj, view.getExpandState());
            }
        }

        @Override
        public void onShrink(ExpandableTextView2 view) {
            Object obj = view.getTag();
            if(obj != null && obj instanceof Integer){
                mPositionsAndStates.put((Integer)obj, view.getExpandState());
            }
        }
    }


    public class  Myadapter2 extends BaseQuickAdapter<BookBean, BaseViewHolder>{

        public Myadapter2(int layoutResId, @Nullable List<BookBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BookBean bookBean) {

           helper.setText(R.id. tv_star,bookBean.getStar() + "分");
            helper.setText(R.id.tv_title,bookBean.getTitle());
            helper.setText(R.id.tv_chapter,bookBean.getChapter());
            helper.setText(R.id.tv_desc,bookBean.getDesc());
            Glide.with(RecycleActivity.this)
                    .load(bookBean.getData_src())
//                .placeholder(R.mipmap.ic_progress)
                    .apply(new RequestOptions().centerCrop().error(R.mipmap.ic_error))
                    .into((ImageView) helper.getView(R.id.img_book));
        }
        }

}
