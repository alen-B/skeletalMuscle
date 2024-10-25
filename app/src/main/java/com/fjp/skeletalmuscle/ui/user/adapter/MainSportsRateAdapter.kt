package com.fjp.skeletalmuscle.ui.user.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.ext.dp
import me.hgj.jetpackmvvm.base.appContext


class MainSportsRateAdapter(data: ArrayList<MainSports>, var defaultIndex: Int) :
    BaseQuickAdapter<MainSports, BaseViewHolder>(
        R.layout.item_main_sports_rate, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: MainSports) {
        if(item.type== SportsType.LEG_LIFT){
            holder.setImageResource(R.id.sportTypeIv,R.drawable.main_sports_leg)
            holder.setImageResource(R.id.sportTypeExpandIv,R.drawable.main_sports_leg)
            holder.setText(R.id.sportNameTv, "高抬腿")
            holder.setText(R.id.sportRateTv, "98")
            holder.setTextColor(R.id.sportRateTv, ContextCompat.getColor(appContext,R.color.color_blue))
        }else if(item.type== SportsType.DUMBBELL){
            holder.setText(R.id.sportNameTv, "哑铃")
            holder.setText(R.id.sportRateTv, "68")
            holder.setTextColor(R.id.sportRateTv, ContextCompat.getColor(appContext,R.color.color_ffc019))
            holder.setImageResource(R.id.sportTypeIv,R.drawable.main_sports_weightlifting)
            holder.setImageResource(R.id.sportTypeExpandIv,R.drawable.main_sports_weightlifting)
        }else{
            holder.setText(R.id.sportNameTv, "平板支撑")
            holder.setText(R.id.sportRateTv, "88")
            holder.setTextColor(R.id.sportRateTv, ContextCompat.getColor(appContext,R.color.color_ff574c))
            holder.setImageResource(R.id.sportTypeIv,R.drawable.main_sports_flat_support)
            holder.setImageResource(R.id.sportTypeExpandIv,R.drawable.main_sports_flat_support)
        }
        if(defaultIndex == holder.bindingAdapterPosition ){
            holder.setVisible(R.id.defaultView,false)
            holder.setVisible(R.id.sportNameTv,true)
            holder.setVisible(R.id.sportTypeExpandIv,true)
            holder.setVisible(R.id.activeLL,true)
            holder.setVisible(R.id.highEfficiencyLL,true)
            holder.setVisible(R.id.heartLL,true)
            holder.setVisible(R.id.extremeBreakthroughLL,true)
            holder.itemView.layoutParams=ConstraintLayout.LayoutParams(300.dp.toInt(), 200.dp.toInt())
        }else{
            holder.setVisible(R.id.defaultView,true)
            holder.setVisible(R.id.sportNameTv,false)
            holder.setVisible(R.id.sportTypeExpandIv,false)
            holder.itemView.layoutParams=ConstraintLayout.LayoutParams(70.dp.toInt(), 200.dp.toInt())
            holder.setVisible(R.id.activeLL,false)
            holder.setVisible(R.id.highEfficiencyLL,false)
            holder.setVisible(R.id.heartLL,false)
            holder.setVisible(R.id.extremeBreakthroughLL,false)
        }
//        holder.setText(R.id.contentTv,item)
        holder.itemView.setOnClickListener {
            defaultIndex = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }
    }
}


