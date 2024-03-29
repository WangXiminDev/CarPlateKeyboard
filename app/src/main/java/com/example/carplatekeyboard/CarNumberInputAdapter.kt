package com.example.carplatekeyboard

import android.graphics.drawable.Drawable
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.exmample.carplatekeyboard.R

/**
 * @Description 车牌号输入列表适配器
 * @Author wangximin
 */
class CarNumberInputAdapter: BaseQuickAdapter<CarNumberInputData, BaseViewHolder>(R.layout.item_number) {

    private val mNormalDrawable: Drawable?
    private val mSelectedDrawable: Drawable?
    init {
        val context = MyApplication.getApplication()
        mNormalDrawable = ContextCompat.getDrawable(context, R.drawable.shape_bg_car_number_normal)
        mSelectedDrawable = ContextCompat.getDrawable(context, R.drawable.shape_bg_car_number_selected)
    }

    override fun convert(holder: BaseViewHolder, item: CarNumberInputData) {
        (holder.itemView as EditText).apply {
            hint = item.hint
            setText(item.text)
            background = if (item.isSelected) mSelectedDrawable else mNormalDrawable
        }
    }
}