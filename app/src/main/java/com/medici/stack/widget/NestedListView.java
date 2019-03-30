package com.medici.stack.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * @author  mrmedici
 * @desc 自定义ListView，用于ScrollView，显示列表
 */
public class NestedListView extends ListView {

    public NestedListView(Context context) {
        super(context);
    }

    public NestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);	}
}
