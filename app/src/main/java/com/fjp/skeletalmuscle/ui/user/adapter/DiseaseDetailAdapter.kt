package com.fjp.skeletalmuscle.ui.user.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil


class DiseaseDetailAdapter(data: ArrayList<String>, var defaultIndex: Int, var clickItem: (item: String, position: Int) -> Unit) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_disease_detail, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        if (defaultIndex == holder.bindingAdapterPosition) {
            holder.setTextColorRes(R.id.contentTv, R.color.white)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_yellow_round_20)
        } else {
            holder.setTextColorRes(R.id.contentTv, R.color.color_1c1c1c)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_gray_round_20)
        }
        holder.setText(R.id.contentTv, item)
        holder.itemView.setOnClickListener {
            defaultIndex = holder.adapterPosition
            clickItem.invoke(item, defaultIndex)
            notifyDataSetChanged()
        }
    }
}


