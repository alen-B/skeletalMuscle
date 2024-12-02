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
import coil.load
import com.fjp.skeletalmuscle.R

/**
 *Author:Mr'x
 *Time:2024/11/1
 *Description:
 */
class SettingItemLayout(context: Context, attrs: AttributeSet) : LinearLayoutCompat(context, attrs) {
    lateinit var titleTv: TextView
    lateinit var valueTv: TextView
    lateinit var avatarIv: ImageView
    lateinit var switch: Switch
    lateinit var listener: CompoundButton.OnCheckedChangeListener

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_item_setting, this);
        val typeAttay = context.obtainStyledAttributes(attrs, R.styleable.SettingItemLayout)
        val title = typeAttay.getString(R.styleable.SettingItemLayout_title)
        val value = typeAttay.getString(R.styleable.SettingItemLayout_value)
        val roundTop = typeAttay.getBoolean(R.styleable.SettingItemLayout_roundTop, false)
        val roundBottom = typeAttay.getBoolean(R.styleable.SettingItemLayout_roundBottom, false)
        typeAttay.recycle()
        val mainLL = findViewById<LinearLayoutCompat>(R.id.mainLl)
        avatarIv = findViewById<ImageView>(R.id.avatarIv)
        titleTv = findViewById(R.id.titleTv)
        valueTv = findViewById(R.id.valueTv)
        switch = findViewById(R.id.sch)
        titleTv.text = title
        valueTv.text = value
        if (roundTop) {
            mainLL.setBackgroundResource(R.drawable.bg_white_top_round_20)
        }
        if (roundBottom) {
            mainLL.setBackgroundResource(R.drawable.bg_white_bottom_round_20)
        }
        if (!roundTop && !roundBottom) {
            mainLL.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
        if (roundTop && roundBottom) {
            mainLL.setBackgroundResource(R.drawable.bg_white_round_20)
        }

        switch.setOnCheckedChangeListener { compoundButton, b ->
            if (this@SettingItemLayout::listener.isInitialized) {
                listener.onCheckedChanged(compoundButton, b)
            }
        }

    }

    fun setValue(value: String) {
        valueTv.text = value
    }

    fun setTitle(title: String) {
        titleTv.text = title
    }

    fun showSwitch(listener: CompoundButton.OnCheckedChangeListener) {
        switch.visibility = View.VISIBLE
        valueTv.visibility = View.GONE
        this.listener = listener
    }

    fun setSwitchStatus(isChecked: Boolean) {
        switch.isChecked = isChecked
    }

    fun setAvatarIv(path: String) {
        avatarIv.visibility = View.VISIBLE
        avatarIv.load(path)
    }
}