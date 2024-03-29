package com.example.carplatekeyboard

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.carplatekeyboard.CarNumberLayoutManager.Companion.ITEM_COMPLETE
import com.example.carplatekeyboard.CarNumberLayoutManager.Companion.ITEM_DELETE
import com.example.carplatekeyboard.CarNumberLayoutManager.Companion.ITEM_NUMBER
import com.example.carplatekeyboard.CarNumberLayoutManager.Companion.ITEM_OTHER
import com.exmample.carplatekeyboard.databinding.DialogCarNumberBinding

/**
 * @Description 车牌选择弹窗
 * @Author wangximin
 */
class CarNumberDialog(
    private val number: String = "",
    private val okCallback: (String) -> Unit
) : DialogFragment() {

    /** 键盘上数字和字符集合 */
    private val keyboardCharList = listOf(
        KeyboardItem("1", ITEM_NUMBER), KeyboardItem("2", ITEM_NUMBER), KeyboardItem("3", ITEM_NUMBER), KeyboardItem("4", ITEM_NUMBER), KeyboardItem("5", ITEM_NUMBER),
        KeyboardItem("6", ITEM_NUMBER), KeyboardItem("7", ITEM_NUMBER), KeyboardItem("8", ITEM_NUMBER), KeyboardItem("9", ITEM_NUMBER), KeyboardItem("0", ITEM_NUMBER),

        KeyboardItem("Q"), KeyboardItem("W"), KeyboardItem("E"), KeyboardItem("R"), KeyboardItem("T"), KeyboardItem("Y"), KeyboardItem("U"), KeyboardItem("O", enabled = false), KeyboardItem("P"),
        KeyboardItem("A"), KeyboardItem("S"), KeyboardItem("D"), KeyboardItem("F"), KeyboardItem("G"), KeyboardItem("H"), KeyboardItem("J"), KeyboardItem("K"), KeyboardItem("L"),
        KeyboardItem("Z"), KeyboardItem("X"), KeyboardItem("C"), KeyboardItem("V"), KeyboardItem("B"), KeyboardItem("N"), KeyboardItem("M"),

        KeyboardItem("✖", ITEM_DELETE),
        KeyboardItem("港", ITEM_OTHER, false),
        KeyboardItem("澳", ITEM_OTHER, false),
        KeyboardItem("完成", ITEM_COMPLETE)
    )

    /** 键盘上省份简称集合 */
    private val keyboardProvinceList = listOf(
        KeyboardItem("京"), KeyboardItem("津"), KeyboardItem("渝"), KeyboardItem("沪"), KeyboardItem("冀"), KeyboardItem("晋"), KeyboardItem("辽"), KeyboardItem("吉"), KeyboardItem("黑"),
        KeyboardItem("苏"), KeyboardItem("浙"), KeyboardItem("皖"), KeyboardItem("闽"), KeyboardItem("赣"), KeyboardItem("鲁"), KeyboardItem("豫"), KeyboardItem("鄂"), KeyboardItem("湘"),
        KeyboardItem("琼"), KeyboardItem("粤"), KeyboardItem("川"), KeyboardItem("贵"), KeyboardItem("云"), KeyboardItem("陕"), KeyboardItem("甘"), KeyboardItem("青"), KeyboardItem("蒙"),
        KeyboardItem("桂"), KeyboardItem("宁"), KeyboardItem("新"), KeyboardItem("藏"),
        KeyboardItem("✖", ITEM_DELETE),
        KeyboardItem("完成", ITEM_COMPLETE)
    )

    // 当前选中的车牌输入位置
    private var mCurrentPosition = 0

    private val mKeyBoardAdapter = KeyBoardAdapter().apply {
        setOnItemClickListener { adapter, view, position ->
            keyboardItemClick(adapter, view, position)
        }
    }

    private val mInputAdapter = CarNumberInputAdapter()

    private val inputDataList = mutableListOf<CarNumberInputData>()

    private lateinit var binding: DialogCarNumberBinding

    override fun onStart() {
        super.onStart()
        setDialogParams()
    }

    private fun setDialogParams() {
        if (dialog != null) {
            val window = dialog!!.window
            if (window != null) {
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params = window.attributes
                params.dimAmount = 0.5f
                params.gravity = Gravity.BOTTOM
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                window.attributes = params
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCarNumberBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvCarNumber.adapter = mInputAdapter
            rvCarNumber.avoidFlashing()

            rvList.adapter = mKeyBoardAdapter
            rvList.avoidFlashing()
        }
        initData()
        setViewListeners()
    }

    private fun initData() {
        val count = if (number.length >= NEW_ENERGY_COUNT) NEW_ENERGY_COUNT else NORMAL_COUNT
        changeEnergyType(count >= NEW_ENERGY_COUNT)
        repeat(count) {
            val text = number.getOrNull(it)?.toString().orEmpty()
            val hint = if (it == 0) "省" else ""
            val selected = when {
                it == 0 && text.isEmpty() -> true
                it == count - 1 && text.isNotEmpty() -> true
                else -> false
            }
            inputDataList.add(CarNumberInputData(text, hint, selected))
        }
        if (number.isEmpty()) {
            mKeyBoardAdapter.setList(keyboardProvinceList)
        } else {
            mCurrentPosition = inputDataList.lastIndex
            mKeyBoardAdapter.setList(keyboardCharList)
            setSpecialKeyEnabled(true)
        }
        mInputAdapter.setList(inputDataList)
    }

    private fun setViewListeners() {
        binding.apply {
            tvChange.setOnClickListener {
                if (tvChange.isChecked) {
                    changeEnergyType(false)
                    mInputAdapter.data.removeLast()
                    if (mCurrentPosition == NEW_ENERGY_COUNT - 1) {
                        mInputAdapter.data.last().isSelected = true
                        setSpecialKeyEnabled(true)
                        mCurrentPosition--
                        mInputAdapter.notifyItemRangeChanged(mInputAdapter.data.lastIndex - 1, 2)
                    } else {
                        mInputAdapter.notifyItemRemoved(mInputAdapter.data.lastIndex)
                    }
                } else {
                    changeEnergyType(true)
                    val newItem = CarNumberInputData()
                    mInputAdapter.data.add(newItem)
                    if (mCurrentPosition == NORMAL_COUNT - 1) {
                        val currentItem = mInputAdapter.data[mCurrentPosition]
                        if (currentItem.text.isNotEmpty()) {
                            if (currentItem.text == "港" || currentItem.text == "澳") {
                                currentItem.text = ""
                                setSpecialKeyEnabled(false)
                            } else {
                                currentItem.isSelected = false
                                newItem.isSelected = true
                                setSpecialKeyEnabled(true)
                                mCurrentPosition++
                            }
                        }
                        mInputAdapter.notifyItemRangeChanged(mInputAdapter.data.lastIndex - 1, 2)
                    } else {
                        mInputAdapter.notifyItemInserted(mInputAdapter.data.lastIndex)
                    }
                }
            }
        }
    }

    /**
     * 切换能源类型
     */
    private fun changeEnergyType(newEnergy: Boolean) {
        binding.apply {
            val gridLayoutManager = rvCarNumber.layoutManager as GridLayoutManager
            if (newEnergy) {
                tvChange.isChecked = true
                tvChange.text = "切换为燃油车"
                gridLayoutManager.spanCount = NEW_ENERGY_COUNT
            } else {
                tvChange.isChecked = false
                tvChange.text = "切换为新能源"
                gridLayoutManager.spanCount = NORMAL_COUNT
            }
        }
    }

    private fun keyboardItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val item = adapter.getItem(position) as KeyboardItem
        if (!item.enabled) {
            return
        }
        val inputItems = mInputAdapter.data
        when (adapter.getItemViewType(position)) {
            ITEM_DELETE -> {
                deleteInputData()
            }
            ITEM_COMPLETE -> {
                val lastItem = inputItems.last()
                if (lastItem.text.isEmpty()) {
                    Toast.makeText(requireContext(), "请输入完整车牌号", Toast.LENGTH_SHORT).show()
                    return
                }
                var number = ""
                inputItems.forEach {
                    number = number.plus(it.text)
                }
                dismiss()
                okCallback.invoke(number)
            }
            else -> {
                addInputData(item.text)
            }
        }
    }

    /**
     * 添加输入内容
     */
    private fun addInputData(inputText: String) {
        val inputItems = mInputAdapter.data
        when (mCurrentPosition) {
            0 -> setNumberEnabled(enabled = false, false)
            1 -> setNumberEnabled(enabled = true, true)
            inputItems.lastIndex - 1 -> setSpecialKeyEnabled(true)
        }
        // 获取当前输入框，将键盘文本赋值给此输入框
        val currentItem = inputItems[mCurrentPosition]
        currentItem.text = inputText
        // 判断输入框索引是否为最后一个
        // true：只更新当前输入框文本
        // false：将当前输入框状态改为未选中，下个输入框状态改为选中，更新两个输入框状态和文本；索引+1
        if (mCurrentPosition != inputItems.lastIndex) {
            currentItem.isSelected = false
            val nextItem = inputItems[mCurrentPosition + 1]
            nextItem.isSelected = true
            mInputAdapter.notifyItemRangeChanged(mCurrentPosition, 2)
            mCurrentPosition++
        } else {
            mInputAdapter.notifyItemChanged(mCurrentPosition)
        }
    }

    /**
     * 删除输入内容
     */
    private fun deleteInputData() {
        val inputItems = mInputAdapter.data
        when (mCurrentPosition) {
            inputItems.lastIndex -> setSpecialKeyEnabled(false)
            2 -> setNumberEnabled(enabled = false, true)
            1 -> mKeyBoardAdapter.setList(keyboardProvinceList)
        }
        // 获取当前输入框，将输入框文本清空
        val currentInputItem = inputItems[mCurrentPosition]
        currentInputItem.text = ""
        // 判断输入框索引是否大于0
        // true：只更新当前输入框文本
        // false：将当前输入框状态改为未选中，上个输入框状态改为选中，更新两个输入框状态和文本；索引-1
        if (mCurrentPosition > 0) {
            currentInputItem.isSelected = false
            val previousInputItem = inputItems[mCurrentPosition - 1]
            previousInputItem.isSelected = true
            mInputAdapter.notifyItemRangeChanged(mCurrentPosition - 1, 2)
            mCurrentPosition--
        } else {
            mInputAdapter.notifyItemChanged(0)
        }
    }

    /**
     * 设置键盘上的数字是否可点击
     * @param enabled 数字是否可点击：true-是，false-否
     * @param onlyRefreshNumber 是否只刷新数字部分：true-是，false：更新集合中数字可点击状态，并将此集合赋值给键盘适配器
     */
    private fun setNumberEnabled(enabled: Boolean, onlyRefreshNumber: Boolean) {
        keyboardCharList.take(10).forEach {
            it.enabled = enabled
        }
        if (onlyRefreshNumber) {
            mKeyBoardAdapter.notifyItemRangeChanged(0, 10)
        } else {
            mKeyBoardAdapter.setList(keyboardCharList)
        }
    }

    /**
     * 设置特殊键是否可点击，普通车牌键盘只判断"港"、"澳"
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setSpecialKeyEnabled(enabled: Boolean) {
        val lastIndex = keyboardCharList.lastIndex
        val list = keyboardCharList.filter { it.text == "港" || it.text == "澳" }
        list.forEach {
            it.enabled = enabled
        }
        mKeyBoardAdapter.notifyItemRangeChanged(lastIndex - list.size, list.size)
    }

    companion object {
        /** 普通燃油车车牌位数 */
        const val NORMAL_COUNT = 7
        /** 新能源车牌位数 */
        const val NEW_ENERGY_COUNT = 8
    }
}