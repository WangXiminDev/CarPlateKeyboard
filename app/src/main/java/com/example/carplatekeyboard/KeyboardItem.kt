package com.example.carplatekeyboard

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @Description KeyboardItem
 * @Author wangximin
 */
data class KeyboardItem(
    /** 键盘文本 */
    val text: String,
    /** 文本类型 */
    val type: Int = CarNumberLayoutManager.ITEM_CHAR,
    /** 是否可点击 */
    var enabled: Boolean = true
) : MultiItemEntity {
    override val itemType: Int
        get() = type
}