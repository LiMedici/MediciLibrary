package com.medici.stack.widget.recycler;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * ***************************************
 *
 * @desc: 允许添加头布局,尾布局的Adapter,MVC,MVP架构中推荐使用
 *
 * ***************************************
 */
public abstract class HFRecyclerAdapter<Data> extends RecyclerAdapter<Data>{

    /**
     * 定义的头部类型
     */
    private static final int BASE_ITEM_TYPE_HEADER = 0x11111;

    /**
     * 定义的尾部类型
     */
    private static final int BASE_ITEM_TYPE_FOOTER = 0x22222;

    /**
     * 包装头部View的容器
     */
    protected final SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    /**
     * 包装尾部View的容器
     */
    protected final SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)){
            // 如果当前显示的Position位置是HeaderView,返回Header Type
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPosition(position)){
            // 如果当前显示的Position位置是HeaderView,返回Footer Type
            return mFooterViews.keyAt(position - getHeaderCount() - getItemCount());
        }

        // 获取真实的position位置
        int realPosition = position - getHeaderCount();
        return super.getItemViewType(realPosition);
    }

    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null){
            // 如果当前是头部View类型
            HeaderAndFooterHolder holder = new HeaderAndFooterHolder(mHeaderViews.get(viewType));
            return holder;

        } else if (mFooterViews.get(viewType) != null){
            // 如果当前是尾部View类型
            ViewHolder holder = new HeaderAndFooterHolder<>(mFooterViews.get(viewType));
            return holder;
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        // 如果是头部View类型
        if (isHeaderViewPosition(position)){
            return;
        }

        // 如果时尾部View类型
        if (isFooterViewPosition(position)){
            return;
        }

        int realPosition = position - getHeaderCount();

        super.onBindViewHolder(holder, realPosition);
    }

    /**
     * 添加一个头布局
     * @param headerView 头布局View
     */
    public void addHeaderView(View headerView){
        int nextPosition = mHeaderViews.size();
        mHeaderViews.append(nextPosition + BASE_ITEM_TYPE_HEADER,headerView);
        notifyItemInserted(nextPosition);
    }

    /**
     * 删除一个头布局
     * @param headerView 布局View
     */
    public void removeHeaderView(View headerView){
        int index = mHeaderViews.indexOfValue(headerView);

        if(index == -1){
            return;
        }

        mHeaderViews.remove(index);

        // 获取到在RecyclerView的位置
        int position = index - BASE_ITEM_TYPE_HEADER;
        if(position < 0 || position > index){
            notifyItemRemoved(position);
        }

    }

    /**
     * 根据坐标删除一个头布局
     * @param index 布局View坐标
     */
    public void removeHeaderView(int index){
        if(index < 0 || index >= getHeaderCount()){
            return;
        }
        mHeaderViews.remove(index + BASE_ITEM_TYPE_HEADER);
        notifyItemRemoved(index);
    }



    /**
     * 添加一个尾布局
     * @param footerView 尾布局View
     */
    public void addFooterView(View footerView){
        int position = mFooterViews.size();
        mFooterViews.append(position + BASE_ITEM_TYPE_FOOTER,footerView);
        notifyItemInserted(position);
    }

    /**
     * 删除一个头布局
     * @param headerView 布局View
     */
    public void removeFooterView(View headerView){
        int index = mFooterViews.indexOfValue(headerView);
        if(index == -1){
            return;
        }

        int position = index - BASE_ITEM_TYPE_FOOTER;

        mFooterViews.remove(position);
        notifyItemRemoved(getHeaderCount() + getItemCount() + position);
    }

    /**
     * 根据坐标删除一个头布局
     * @param position 布局View坐标
     */
    public void removeFooterView(int position){
        if(position < 0 || position >= getFooterCount()){
            return;
        }

        int index = position + BASE_ITEM_TYPE_FOOTER;

        mHeaderViews.remove(index);

        notifyItemRemoved(getHeaderCount() + getItemCount() + position);
    }

    /**
     * 是否是显示头部View的position
     * @param position Adapter坐标
     * @return true 显示头部View
     */
    protected boolean isHeaderViewPosition(int position){
        return position < getHeaderCount();
    }

    protected boolean isFooterViewPosition(int position){
        return position >= getHeaderCount() + getItemCount();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + getHeaderCount() + getFooterCount();
    }

    /**
     * 返回头部View的个数
     * @return 总数
     */
    @Override
    protected int getHeaderCount(){
        return mHeaderViews.size();
    }

    /**
     * 返回尾部View的个数
     * @return 总数
     */
    @Override
    protected int getFooterCount(){
        return mFooterViews.size();
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position){
                    int viewType = getItemViewType(position);
                    if (mHeaderViews.get(viewType) != null){
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(viewType) != null){
                        return gridLayoutManager.getSpanCount();
                    }

                    if (spanSizeLookup != null){
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder<Data> holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();

        if (isHeaderViewPosition(position) || isFooterViewPosition(position)){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * 头部尾部的ViewHolder
     * @param <Data> 范型
     */
    private static class HeaderAndFooterHolder<Data> extends RecyclerAdapter.ViewHolder<Data>{
        public HeaderAndFooterHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Data data) {

        }
    }

}
