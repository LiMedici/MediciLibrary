package com.medici.stack.widget.recycler;

/**
 * @author cnbilzh
 * @desc 修改某个Item项的回调接口定义,MVVM+DataBinding架构中推荐使用
 */
public interface AdapterBindingCallback<Data> {
    /**
     * 列表发生更新操作
     * @param data 更新的数据实体
     * @param holder 更新的Holder
     */
    void update(Data data, RecyclerBindingAdapter.ViewHolder<Data> holder);
}
