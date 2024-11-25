package com.fjp.skeletalmuscle.app.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.fjp.skeletalmuscle.R

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SportsRecordItemLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {
    lateinit var titleTv: TextView
    lateinit var valueTv: TextView
    lateinit var unitTv: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_sports_record_all_sports_item, this)
        val typeAttay = context.obtainStyledAttributes(attrs, R.styleable.SportsRecordItemLayout)
        val title = typeAttay.getString(R.styleable.SportsRecordItemLayout_title)
        val unit = typeAttay.getString(R.styleable.SportsRecordItemLayout_unit)
        typeAttay.recycle()
        valueTv= findViewById(R.id.valueTv)
        titleTv = findViewById(R.id.titleTv)
        unitTv = findViewById(R.id.unitTv)
        titleTv.text = title
        unitTv.text = unit
    }

    fun setValue(value: String) {
        valueTv.text = value
    }

    fun setTitle(title: String) {
        titleTv.text = title
    }

    fun setUnit(unit: String) {
        unitTv.text = unit
    }
}