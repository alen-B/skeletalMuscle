package com.fjp.skeletalmuscle.ui.assessment.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil


class WaistlineAndWeightAdapter(data: ArrayList<Int>, val isWeight: Boolean, var defaultIndex: Int, var clickItem: (item: Int, position: Int) -> Unit) : BaseQuickAdapter<Int, BaseViewHolder>(R.layout.item_waistline_weight, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: Int) {
        if (defaultIndex === holder.adapterPosition) {
            holder.setVisible(R.id.singleSelectedIv, true)
            holder.setTextColorRes(R.id.contentTv, R.color.white)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_selected)
        } else {
            holder.setTextColorRes(R.id.contentTv, R.color.color_1c1c1c)
            holder.setVisible(R.id.singleSelectedIv, false)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_gray_round_20)
        }
        if (isWeight) {
            holder.setText(R.id.contentTv, "${item}kg")
        } else {
            holder.setText(R.id.contentTv, "${item}cm")
        }
        holder.itemView.setOnClickListener {
            defaultIndex = holder.adapterPosition
            clickItem.invoke(item, defaultIndex)
            notifyDataSetChanged()
        }
    }
}


