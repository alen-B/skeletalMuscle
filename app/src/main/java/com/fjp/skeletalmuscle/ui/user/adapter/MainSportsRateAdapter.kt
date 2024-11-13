package com.fjp.skeletalmuscle.ui.user.adapter

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.util.dp2px


class MainSportsRateAdapter(data: ArrayList<MainSports>, var defaultIndex: Int) :
    BaseQuickAdapter<MainSports, BaseViewHolder>(
        R.layout.item_main_sports_rate, data
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: MainSports) {
        if(item.type== SportsType.HIGH_KNEE){
            holder.setImageResource(R.id.sportTypeIv,R.drawable.main_sports_leg)
            holder.setImageResource(R.id.sportTypeExpandIv,R.drawable.main_sports_leg)
            holder.setText(R.id.sportNameTv, "高抬腿")
            holder.setText(R.id.sportRateTv, item.sports.score.toString())
            holder.getView<RoundedProgressBar>(R.id.warmupTimePB).setProgressPercentage(item.sports.warmupTime/(item.sports.time/1000)*100,true)
            holder.getView<RoundedProgressBar>(R.id.fatBurningTimePb).setProgressPercentage(item.sports.fatBurningTime/(item.sports.time/1000)*100,true)
            holder.getView<RoundedProgressBar>(R.id.cardioTimePb).setProgressPercentage(item.sports.cardioTime/(item.sports.time/1000)*100,true)
            holder.getView<RoundedProgressBar>(R.id.breakTimePB).setProgressPercentage(item.sports.breakTime/(item.sports.time/1000)*100,true)
            val totalTime = (item.sports.time /(1000 * 60f)).toInt().toString()+"min"
            holder.setText(R.id.warmupTimeMinTv, totalTime)
            holder.setText(R.id.fatBurningTimeMinTv, totalTime)
            holder.setText(R.id.cardioTimeTotalMinTv, totalTime)
            holder.setText(R.id.breakTimeTotalMinTv, totalTime)
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

        if(defaultIndex == holder.adapterPosition ){
            val animator = createWidthIncreaseAnimation(holder.getView(R.id.itemCl), appContext.dp2px(70),appContext.dp2px(300),500)
            animator.start()
            holder.setVisible(R.id.defaultView,false)
            holder.setVisible(R.id.sportNameTv,true)
            holder.setVisible(R.id.sportTypeExpandIv,true)
            holder.setVisible(R.id.activeLL,true)
            holder.setVisible(R.id.highEfficiencyLL,true)
            holder.setVisible(R.id.heartLL,true)
            holder.setVisible(R.id.extremeBreakthroughLL,true)
            holder.itemView.layoutParams=ConstraintLayout.LayoutParams(appContext.dp2px(300), appContext.dp2px(200))

        }else{
            holder.setVisible(R.id.defaultView,true)
            holder.setVisible(R.id.sportNameTv,false)
            holder.setVisible(R.id.sportTypeExpandIv,false)
            holder.itemView.layoutParams=ConstraintLayout.LayoutParams(appContext.dp2px(70), appContext.dp2px(200))
            holder.setVisible(R.id.activeLL,false)
            holder.setVisible(R.id.highEfficiencyLL,false)
            holder.setVisible(R.id.heartLL,false)
            holder.setVisible(R.id.extremeBreakthroughLL,false)
        }
//        holder.setText(R.id.contentTv,item)
        holder.itemView.setOnClickListener {
            defaultIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
    }
    fun createWidthIncreaseAnimation(view: View, fromWidth: Int, toWidth: Int, duration: Long): ValueAnimator {
        val animator = ValueAnimator.ofInt(fromWidth, toWidth)
        animator.duration = duration
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams
            if (layoutParams is ViewGroup.LayoutParams) {
                layoutParams.width = value
                view.layoutParams = layoutParams
            }
        }
        return animator
    }
}



