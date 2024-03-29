package com.example.carplatekeyboard

/**
 * @Description CarNumberInputData
 * @Author wangximin
 */
data class CarNumberInputData(
    /** 输入文本 */
    var text: String = "",
    /** 提示文本 */
    val hint: String = "",
    /** 是否选中 */
    var isSelected: Boolean = false
)
