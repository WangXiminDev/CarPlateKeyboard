package com.example.carplatekeyboard

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * @Description CarNumberLayoutManager
 * @Author wangximin
 */
class CarNumberLayoutManager : RecyclerView.LayoutManager() {

    private val mColumnPadding = 12
    private val mRowPadding = 20

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        recycler ?: return
        detachAndScrapAttachedViews(recycler)
        // 是否存在数字类型
        var existNumber = false
        // 数字(一行，共10位）宽度
        val numItemWidth = (width - mColumnPadding * 11) / 10
        // 省份和字母（每行9位）宽度
        val charItemWidth = ((width - mColumnPadding * 10) / 9f).roundToInt()
        // 已用宽度
        var widthUsed = mColumnPadding
        // 已用高度
        var heightUsed = mColumnPadding
        // 是否为每行的最后一个子项
        var isRowLastChild: Boolean
        for (i in 0 until itemCount) {
            val child = recycler.getViewForPosition(i)
            addView(child)
            // item视图类型
            val type = getItemViewType(child)
            val childWidth = when (type) {
                ITEM_NUMBER -> {
                    existNumber = true
                    numItemWidth
                }
                ITEM_CHAR -> charItemWidth
                ITEM_DELETE -> width - charItemWidth * 7 - mColumnPadding * 9
                ITEM_COMPLETE -> width - charItemWidth * 6 - mColumnPadding * 8
                else -> charItemWidth * 2 + mColumnPadding
            }
            isRowLastChild = when (type) {
                ITEM_NUMBER -> i > 0 && i % 9 == 0
                ITEM_CHAR -> i > 0 && (if (existNumber) i % 9 == 0 else i % 9 == 8)
                else -> false
            }
            val measureWidthUsed = if (isRowLastChild) widthUsed + mColumnPadding else width - childWidth
            measureChildWithMargins(child, measureWidthUsed, 0)
            val measuredW = getDecoratedMeasuredWidth(child)
            val measuredH = getDecoratedMeasuredHeight(child)
            val childBottom = heightUsed + measuredH + mRowPadding
            when (type) {
                ITEM_DELETE, ITEM_COMPLETE -> {
                    val right = width - mColumnPadding
                    layoutDecoratedWithMargins(child, right - measuredW, heightUsed, right, childBottom)
                    widthUsed = width
                }
                else -> {
                    layoutDecoratedWithMargins(child, widthUsed, heightUsed, widthUsed + measuredW, childBottom)
                    widthUsed += measuredW + mColumnPadding
                }
            }
            // 如果已用宽度等于父view宽度，表示当前行的子view已全部摆放完毕
            // 所以需要在新的一行摆放下个子view，已用宽度置为初始值，已用高度等于当前行子view的底部坐标 + 行间距
            if (widthUsed == width) {
                widthUsed = mColumnPadding
                heightUsed = childBottom
            }
        }
    }

    companion object {
        /** 键盘上的数字 */
        const val ITEM_NUMBER = 0
        /** 键盘上的汉字或英文 */
        const val ITEM_CHAR = 1
        /** 键盘上的删除按钮 */
        const val ITEM_DELETE = 2
        /** 键盘上的完成按钮 */
        const val ITEM_COMPLETE = 3
        /** 键盘上其他类型的按钮 */
        const val ITEM_OTHER = 4
    }
}
