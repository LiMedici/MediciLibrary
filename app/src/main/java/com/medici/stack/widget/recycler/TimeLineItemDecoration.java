package com.medici.stack.widget.recycler;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.medici.stack.R;
import com.medici.stack.util.UIUtil;
import com.medici.stack.util.blankj.SizeUtil;

/**
 * **********************************
 *
 * @desc RecyclerView时间轴显示的Decoration
 * <p>
 * **********************************
 */
public class TimeLineItemDecoration extends RecyclerView.ItemDecoration {

    private static final int LINE_STROKE_WIDTH = 1;

    // 写右边字的画笔(具体信息)
    private Paint mPaint;
    private Paint mLinePaint;

    // 左 上偏移长度
    private int mLeftInterval;
    private int mTopInterval;

    // 轴点半径
    private int mCircleRadius;

    // footer item数目
    private int footerCount;

    public TimeLineItemDecoration(int leftInterval, int topInterval, int circleRadius, int footerCount) {
        this.mLeftInterval = leftInterval;
        this.mTopInterval = topInterval;
        this.mCircleRadius = circleRadius;
        this.footerCount = footerCount;


        // 轴点画笔(红色)
        mPaint = new Paint();
        mLinePaint = new Paint();
        mLinePaint.setColor(UIUtil.getColor(R.color.colorGary));
        mLinePaint.setStrokeWidth(SizeUtil.dp2px(LINE_STROKE_WIDTH));
        mPaint.setColor(UIUtil.getColor(R.color.colorGary));
    }

    @Override
    /**
     * 作用：设置ItemView 左 & 上偏移长度
     */
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 设置ItemView的左 & 上偏移长度分别为200 px & 50px,即此为onDraw()可绘制的区域
        outRect.set(mLeftInterval, mTopInterval, 0, 0);
    }

    /**
     * 作用:在间隔区域里绘制时光轴线 & 时间文本
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount() - footerCount;

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {

            // 获取每个Item对象
            View child = parent.getChildAt(i);

            // 绘制轴点
            // 轴点 = 圆 = 圆心(x,y)
            float centerX = child.getLeft() - mLeftInterval / 2;
            float centerY = child.getTop() - mTopInterval + (mTopInterval + child.getHeight()) / 2;
            // 绘制轴点圆
            c.drawCircle(centerX, centerY, mCircleRadius, mPaint);

            // 绘制上半轴线
            // 上端点坐标(x,y)
            float upLineUpX = centerX;
            float upLineUpY = child.getTop() - mTopInterval;

            // 下端点坐标(x,y)
            float upLineBottomX = centerX;
            float upLineBottomY = centerY - mCircleRadius;

            // 绘制上半部轴线
            // 如果是第1个Item,就不画上半轴线
            if (i != 0) {
                c.drawLine(upLineUpX, upLineUpY, upLineBottomX, upLineBottomY, mLinePaint);
            }

            // 绘制下半轴线

            // 上端点坐标(x,y)
            float bottomLineUpX = centerX;
            float bottomLineUpY = centerY + mCircleRadius;

            // 下端点坐标(x,y)
            float bottomLineBottomX = centerX;
            float bottomLineBottomY = child.getBottom();

            // 绘制下半部轴线
            // 如果是最后一条,就不画下半轴线
            if (i != childCount - 1) {
                c.drawLine(bottomLineUpX, bottomLineUpY, bottomLineBottomX, bottomLineBottomY, mLinePaint);
            }
        }
    }
}
