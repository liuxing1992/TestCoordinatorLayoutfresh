package com.example.admin.testcoordinatorlayoutfresh;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.example.admin.testcoordinatorlayoutfresh.bean.ItemBean;

/**
 * Description:
 * Data：2018/9/26-14:52
 * Author: ly
 */
public class MySection extends SectionEntity<ItemBean> {

    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(ItemBean s) {
        super(s);
    }

}
