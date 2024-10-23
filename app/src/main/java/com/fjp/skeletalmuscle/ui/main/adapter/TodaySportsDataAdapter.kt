package com.fjp.skeletalmuscle.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil


class TodaySportsDataAdapter(data: ArrayList<String>, var clickItem: (item: String) -> Unit) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_today_sports_data, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.setOnClickListener {
            clickItem.invoke(item)
        }
    }
}


