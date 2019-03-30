package com.medici.stack.widget.recycler;

/**
 * @desc 修改某个Item项的回调接口定义 MVC,MVP架构中推荐使用
 */
public interface AdapterCallback<Data> {
    /**
     * 列表发生更新操作
     * @param data 更新的数据实体
     * @param holder 更新的Holder
     */
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
