package com.fjp.skeletalmuscle.ui.assessment.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil


class AssessmentTypeAdapter(data: ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_assessment_type, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        if(holder.bindingAdapterPosition==0){
            holder.setImageResource(R.id.stepIv, R.drawable.assessment_01)
        }else if(holder.bindingAdapterPosition==1){
            holder.setImageResource(R.id.stepIv, R.drawable.assessment_02)
        }else if(holder.bindingAdapterPosition==2){
            holder.setImageResource(R.id.stepIv, R.drawable.assessment_03)
        }else if(holder.bindingAdapterPosition==3){
            holder.setImageResource(R.id.stepIv, R.drawable.assessment_04)
        }


    }
}


