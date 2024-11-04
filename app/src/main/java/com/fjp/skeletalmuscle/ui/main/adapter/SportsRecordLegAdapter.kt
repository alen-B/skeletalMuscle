package com.fjp.skeletalmuscle.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsRecord


class SportsRecordLegAdapter(data: ArrayList<SportsRecord>) :
    BaseQuickAdapter<SportsRecord, BaseViewHolder>(
        R.layout.item_sports_record_leg, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: SportsRecord) {
        holder.setText(R.id.titleTv,item.name)
        holder.setText(R.id.numberTv,item.value)
        holder.setText(R.id.unitTv,item.unit)

    }
}


