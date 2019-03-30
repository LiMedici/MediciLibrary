package com.medici.stack.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * @desc 自定义ExpandableListView，用于嵌套PullToRefreshView的ScrollView，显示列表
 */
public class NestedExpandableListView extends ExpandableListView {

    public NestedExpandableListView(Context context) {
        super(context);
    }

    public NestedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);	}
}
