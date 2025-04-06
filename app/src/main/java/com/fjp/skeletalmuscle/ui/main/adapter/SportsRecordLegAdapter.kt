package com.fjp.skeletalmuscle.ui.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsRecord


class SportsRecordLegAdapter(data: ArrayList<SportsRecord>) : BaseQuickAdapter<SportsRecord, BaseViewHolder>(R.layout.item_sports_record_leg, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: SportsRecord) {
        holder.setText(R.id.titleTv, item.name)
        if(item.name == "上举次数"){
            holder.setText(R.id.numberTv, item.value)
            holder.setVisible(R.id.dumbellLL,true)
            holder.setVisible(R.id.numberTv,false)
            holder.setVisible(R.id.unitTv,false)
            holder.setText(R.id.rightAccountTv,item.value)
            holder.setText(R.id.leftAccountTv,item.value2)
        }else{
            holder.setVisible(R.id.dumbellLL,false)
            holder.setVisible(R.id.numberTv,true)
            holder.setVisible(R.id.unitTv,true)
            holder.setText(R.id.numberTv, item.value)
            holder.setText(R.id.unitTv, item.unit)
        }

        println("===titleTv:${item.name}")
        println("===value:${item.value}")

    }
}


