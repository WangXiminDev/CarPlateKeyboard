package com.example.carplatekeyboard

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exmample.carplatekeyboard.R

/**
 * @Description 键盘适配器
 * @Author wangximin
 */
class KeyBoardAdapter : BaseMultiItemQuickAdapter<KeyboardItem, BaseViewHolder>() {

    init {
        addItemType(CarNumberLayoutManager.ITEM_NUMBER, R.layout.item_car_number)
        addItemType(CarNumberLayoutManager.ITEM_CHAR, R.layout.item_car_number)
        addItemType(CarNumberLayoutManager.ITEM_DELETE, R.layout.item_car_number)
        addItemType(CarNumberLayoutManager.ITEM_COMPLETE, R.layout.item_car_number)
        addItemType(CarNumberLayoutManager.ITEM_OTHER, R.layout.item_car_number)
    }

    override fun convert(holder: BaseViewHolder, item: KeyboardItem) {
        holder.setText(R.id.tv_name, item.text)
        holder.setEnabled(R.id.tv_name, item.enabled)
    }
}