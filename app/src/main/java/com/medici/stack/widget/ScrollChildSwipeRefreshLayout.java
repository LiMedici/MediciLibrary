package com.medici.stack.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * ***************************************
 *
 * <p>根据google官方Demo 集成的SwipeRefreshLayout 可使用在所有滑动的View上</p>
 * Extends {@link SwipeRefreshLayout} to support non-direct descendant scrolling views.
 * {@link SwipeRefreshLayout} works as expected when a scroll view is a direct child: it triggers
 * the refresh only when the view is on top. This class adds a way {@link #setScrollUpChild} to
 * define which view controls this behavior.
 *
 * ***************************************
 */
public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mScrollUpChild;

    public ScrollChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }
}
