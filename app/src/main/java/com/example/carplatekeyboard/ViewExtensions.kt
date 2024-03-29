package com.example.carplatekeyboard

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * 设置不闪烁 闪烁的主要原因是在通知刷新时，会设置alpha变为0 ，然后变为1
 * */
fun RecyclerView.avoidFlashing() {
    val sa = this.itemAnimator as SimpleItemAnimator
    sa.supportsChangeAnimations = false
    this.itemAnimator = null
}