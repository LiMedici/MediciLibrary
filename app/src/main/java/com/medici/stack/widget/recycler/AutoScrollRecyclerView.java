package com.medici.stack.widget.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.medici.stack.util.blankj.SizeUtil;

/**
 * ***************************************
 *
 * @desc: 由于ScrollToPosition滑动到指定Item,太突兀。所以该View实现了平滑的滑动到指定Item。
 *        并且扩展可以监听软键盘的弹出和收起,发起回调。
 *
 * ***************************************
 */
public class AutoScrollRecyclerView extends RecyclerView {
    /**
     * 阀值
     */
    private static final int THRESHOLD_VALUE = SizeUtil.dp2px(100);

    /**
     * 是否要求滑动到某一项
     */
    private boolean move;

    /**
     * 要求滑动到某一项的坐标
     */
    private int position;

    /**
     * 软键盘状态的监听Listener
     */
    private OnKeyBoardChangeListener onKeyBoardChangeListener;

    public AutoScrollRecyclerView(Context context) {
        this(context,null);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 滑动到某一Item位置
     * @param position 目标位置
     */
    public void autoScrollToPosition(int position){
        // 先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LayoutManager manager = getLayoutManager();
        if(position < 0 || null == manager || !(manager instanceof LinearLayoutManager)){
            return;
        }

        LinearLayoutManager mLayoutManager = (LinearLayoutManager) manager;
        // 第一项
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        // 最后一项
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        // 然后区分情况
        if (position <= firstItem ){
            // 当要置顶的项在当前显示的第一个项的前面时
            scrollToPosition(position);
        }else if ( position <= lastItem ){
            // 当要置顶的项已经在屏幕上显示时
            int top = getChildAt(position - firstItem).getTop();
            scrollBy(0, top);
        }else{
            // 当要置顶的项在当前显示的最后一项的后面时
            scrollToPosition(position);
            // 这里这个变量是用在RecyclerView滚动监听里面的
            this.position = position;
            move = true;
        }
    }

    /**
     * 平滑的滑动到某一Item位置
     * @param position 目标位置
     */
    public void autoSmoothScrollToPosition(int position){
        // 先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LayoutManager manager = getLayoutManager();
        if(position < 0 || null == manager || !(manager instanceof LinearLayoutManager)){
            return;
        }

        LinearLayoutManager mLayoutManager = (LinearLayoutManager) manager;
        // 第一项
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        // 最后一项
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        // 然后区分情况
        if (position <= firstItem ){
            // 当要置顶的项在当前显示的第一个项的前面时
            smoothScrollToPosition(position);
        }else if ( position <= lastItem ){
            // 当要置顶的项已经在屏幕上显示时
            int top = getChildAt(position - firstItem).getTop();
            // 这里滑动是不平滑的
            scrollBy(0, top);
        }else{
            //当要置顶的项在当前显示的最后一项的后面时
            smoothScrollToPosition(position);
            //这里这个变量是用在RecyclerView滚动监听里面的
            this.position = position;
            move = true;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        //在这里进行第二次滚动（最后的100米！）
        if (move){
            move = false;
            // 获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
            LayoutManager manager = getLayoutManager();
            if(null == manager && !(manager instanceof LinearLayoutManager)){
                return;
            }

            LinearLayoutManager mLayoutManager = (LinearLayoutManager) manager;
            int n = position - mLayoutManager.findFirstVisibleItemPosition();

            if ( 0 <= n && n < getChildCount()){
                //获取要置顶的项顶部离RecyclerView顶部的距离
                int top = getChildAt(n).getTop();
                //最后的移动 不平滑的
                scrollBy(0, top);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (onKeyBoardChangeListener != null) {
            // 如果View之前的高度大于现在的高度,则因为软键盘弹起调整高度的原因
            if (oldh > h && Math.abs(oldh - h) > THRESHOLD_VALUE) {
                onKeyBoardChangeListener.onKeyBoardShow();
            } else {
                onKeyBoardChangeListener.onKeyBoardHidden();
            }
        }
    }

    /**
     * 设置监听软键盘的弹起和收起。回调onKeyBoardShow或者onKeyBoardHidden方法。Activity主题必须要设置为adjustResize
     * @param onKeyBoardChangeListener 监听者
     */
    public void setOnKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener){
        this.onKeyBoardChangeListener = onKeyBoardChangeListener;
    }

    /**
     * 软键盘状态监听的接口定义
     */
    public interface OnKeyBoardChangeListener {
        /**
         * 软键盘弹起
         */
        void onKeyBoardShow();

        /**
         * 软键盘隐藏
         */
        void onKeyBoardHidden();
    }

    /**
     * 监听的默认实现
     */
    public static class OnKeyBoardChangeListenerImpl implements OnKeyBoardChangeListener{

        @Override
        public void onKeyBoardShow() {

        }

        @Override
        public void onKeyBoardHidden() {

        }
    }
}
