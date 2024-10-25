package com.fjp.skeletalmuscle.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil


class SprotsRecordLegAdapter(data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_sports_record_leg, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: String) {
//        holder.setText(R.id.titleTv,item)
//        holder.setText(R.id.numberTv,item)
//        holder.setText(R.id.unitTv,item)

    }
}


