package com.fjp.skeletalmuscle.ui.user.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsChild


class SportsChildTypeAdapter(data: ArrayList<SportsChild>, var clickItem: (item: SportsChild) -> Unit) : BaseQuickAdapter<SportsChild, BaseViewHolder>(R.layout.item_sports_child_select, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: SportsChild) {
        if (item.isSelected) {
            holder.setTextColorRes(R.id.contentTv, R.color.white)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_yellow_round_20)
        } else {
            holder.setTextColorRes(R.id.contentTv, R.color.color_1c1c1c)
            holder.setBackgroundResource(R.id.contentTv, R.drawable.bg_gray_round_20)
        }
        holder.setText(R.id.contentTv, item.name)
        holder.itemView.setOnClickListener {

            item.isSelected = !item.isSelected
            clickItem.invoke(item)
            notifyDataSetChanged()
        }
    }

}


