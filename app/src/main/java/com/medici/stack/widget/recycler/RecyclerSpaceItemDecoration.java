package com.medici.stack.widget.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.medici.stack.util.UIUtil;

/**
 * **********************************
 *
 * @RecyclerView布局 Grid 列间隔
 *
 * **********************************
 */
public class RecyclerSpaceItemDecoration extends RecyclerView.ItemDecoration {
    // 设置间距
    private int space;
    // 列数
    private int column;
    // 是否边界留空格
    private boolean leftAndRight;

    public RecyclerSpaceItemDecoration(int column, int space) {
        this.column = column;
        this.space = space;
        // 默认留空格
        this.leftAndRight = true;
        if(column <= 1){
            throw new RuntimeException("recycler grid column value must > 1");
        }
    }

    public RecyclerSpaceItemDecoration(int column,int space,boolean leftAndRight){
        this(column,space);
        this.leftAndRight = leftAndRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 不是第一个的格子和最后一个格子都设一个左边和的右边间距
        // 中间的只设置一半左右边间距
        float density = UIUtil.getResources().getDisplayMetrics().density;
        int margin = (int) (density * space);
        outRect.left = margin;
        outRect.right = margin;
        int position = parent.getChildLayoutPosition(view);
        if (position % column == 0) {
            // 坐标每行第一列
            if(leftAndRight){
                outRect.left = margin;
            }else{
                outRect.left = 0;
            }
            outRect.right = margin / 2;
        }else if(position % column == column - 1){
            // 坐标每行最后一列
            if(leftAndRight){
                outRect.right = margin;
            }else{
                outRect.right = 0;
            }
            outRect.left = margin / 2;
        }else{
            // 中间的其它列
            outRect.left = margin / 2;
            outRect.right = margin / 2;
        }
    }

}
