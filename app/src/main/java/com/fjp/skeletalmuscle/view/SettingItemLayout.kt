package com.fjp.skeletalmuscle.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R
import me.hgj.jetpackmvvm.base.appContext

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SettingItemLayout(context: Context,attrs: AttributeSet):LinearLayoutCompat(context,attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_item_setting, this);
       val typeAttay = context.obtainStyledAttributes(attrs, R.styleable.SettingItemLayout)
        val title= typeAttay.getString(R.styleable.SettingItemLayout_title)
        val value= typeAttay.getString(R.styleable.SettingItemLayout_value)
        val isFirst= typeAttay.getBoolean(R.styleable.SettingItemLayout_isFirst,false)
        val isLast= typeAttay.getBoolean(R.styleable.SettingItemLayout_isLast,false)
        typeAttay.recycle()
        val mainLL = findViewById<LinearLayoutCompat>(R.id.mainLl)
        val titleTv = findViewById<TextView>(R.id.titleTv)
        val valueTv = findViewById<TextView>(R.id.valueTv)
        titleTv.text = title
        valueTv.text = value
        if(isFirst){
            mainLL.setBackgroundResource(R.drawable.bg_white_top_round_20)
        }
        if(isLast){
            mainLL.setBackgroundResource(R.drawable.bg_white_bottom_round_20)
        }
        if(!isLast && !isFirst){
            mainLL.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

    }
}