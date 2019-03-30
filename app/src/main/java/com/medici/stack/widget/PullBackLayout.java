package com.medici.stack.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;


/**
 * @desc 下拉回退的Widget
 */
public class PullBackLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    private PullCallBack pullCallBack;

    private int mMinimumFlingVelocity;

    public void setPullCallBack(PullCallBack pullCallBack) {
        this.pullCallBack = pullCallBack;
    }


    public PullBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        dragHelper = ViewDragHelper.create(this, 1f / 8f, new DragCallBack());

        mMinimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }


    class DragCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.max(0, top);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getHeight();
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            if (pullCallBack != null) {
                pullCallBack.onPullStart();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int slop = yvel > mMinimumFlingVelocity ? getHeight() / 6 : getHeight() / 3;
            if (releasedChild.getTop() > slop) {
                if (pullCallBack != null) {
                    pullCallBack.onPullCompleted();
                }
            }else {
                dragHelper.settleCapturedViewAt(0, 0);
                invalidate();
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float progress = Math.min(1f, ((float)top / (float)getHeight()) * 2f);
            if (pullCallBack != null)
                pullCallBack.onPull(progress);

        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //这么做的目的是当图片缩小时应用会发生下标越界异常，
        // 接着捕捉异常返回false，子View可以继续处理事件分发，应用就不会crash了
        try {
            return dragHelper.shouldInterceptTouchEvent(ev);
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    public interface PullCallBack {

        void onPullStart();

        void onPull(float progress);

        void onPullCompleted();
    }
}