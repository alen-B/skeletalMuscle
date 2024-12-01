package com.fjp.skeletalmuscle.ui.user.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.AnimUtil
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.TodaySportsType
import me.hgj.jetpackmvvm.ext.util.dp2px


class TodaySportsTypeAdapter(data: ArrayList<TodaySportsType>, var defaultIndex: Int, val listener: SelectedSports) : BaseQuickAdapter<TodaySportsType, BaseViewHolder>(R.layout.item_today_sports_type, data) {
    interface SelectedSports {
        fun onSelected(sportsType: TodaySportsType)
    }

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: TodaySportsType) {

        holder.setText(R.id.titleTv, item.title)
        holder.setText(R.id.des, item.des)
        if (item.type == SportsType.HIGH_KNEE) {
            holder.setImageResource(R.id.legIv, R.drawable.select_sports_leg)
        } else if (item.type == SportsType.DUMBBELL) {
            holder.setImageResource(R.id.legIv, R.drawable.select_sports_dumbbell)
        } else if (item.type == SportsType.PLANK) {
            holder.setImageResource(R.id.legIv, R.drawable.select_sports_plank)
        }
        addExerciseIntensityView(holder.getView<LinearLayoutCompat>(R.id.exerciseIntensityLL), item.exerciseIntensity)

        if (item.isDetail) {
            rotateAndSwitchViews(holder.getView(R.id.legIv), holder.getView(R.id.legDetailCl))
        } else {
            if (holder.getView<ConstraintLayout>(R.id.legDetailCl).isVisible) {
                rotateAndSwitchViews(holder.getView(R.id.legDetailCl), holder.getView(R.id.legIv))
            }
        }

        holder.itemView.setOnClickListener {
            if (defaultIndex == holder.bindingAdapterPosition) {
                return@setOnClickListener
            }
            listener.onSelected(item)
            data[defaultIndex].isDetail = false
            item.isDetail = !item.isDetail
            defaultIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    private fun addExerciseIntensityView(view: LinearLayoutCompat, exerciseIntensity: Int) {
        view.removeAllViews()
        val marginLayoutParams = ViewGroup.MarginLayoutParams(context.dp2px(30), context.dp2px(37))
//        marginLayoutParams.leftMargin = context.dp2px(40)
        for (i in 0..exerciseIntensity / 2) {
            val imageview = ImageView(context)
            imageview.layoutParams = marginLayoutParams
            imageview.setImageResource(R.drawable.sports_type_strong_selected)
            imageview.scaleType = ImageView.ScaleType.CENTER
            view.addView(imageview)
        }

        if (exerciseIntensity % 2 == 1) {
            val imageview = ImageView(context)
            imageview.layoutParams = marginLayoutParams
            imageview.setImageResource(R.drawable.sports_type_strong_normal)
            imageview.scaleType = ImageView.ScaleType.CENTER
            view.addView(imageview)
        }
    }

    private fun rotateAndSwitchViews(iv: View, cl: View) {
        AnimUtil.flipAnimatorYViewShow(iv, cl, 500)
    }
}



