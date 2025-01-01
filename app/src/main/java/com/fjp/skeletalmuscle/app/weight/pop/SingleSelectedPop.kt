package com.fjp.skeletalmuscle.app.weight.pop

import android.content.Context
import android.widget.TextView
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.lxj.xpopup.core.BottomPopupView
import io.alterac.blurkit.BlurLayout


/**
 *Author:Mr'x
 *Time:2024/10/29
 *Description:
 */
class SingleSelectedPop(context: Context, var type: SingleSelectType, val listener: Listener) : BottomPopupView(context) {
    private lateinit var blurLayout: BlurLayout
    var curIndex = 0

    interface Listener {
        fun onSelected(data: String, pop: SingleSelectedPop)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.pop_single_selected
    }

    override fun onCreate() {
        super.onCreate()
        val wheelView = findViewById<WheelView>(R.id.wheelview)
        wheelView.setCyclic(false)
        val sureTv = findViewById<TextView>(R.id.sureTv)
        val cancelTv = findViewById<TextView>(R.id.cancelTv)
        val titleTv = findViewById<TextView>(R.id.titleTv)
        if (type == SingleSelectType.HEIGHT) {
            titleTv.setText(R.string.setting_height)
        } else if (type == SingleSelectType.WEIGHT) {
            titleTv.setText(R.string.setting_weight)
        } else if (type == SingleSelectType.WAIST_LINE) {
            titleTv.setText(R.string.setting_waist_lin)
        } else if (type == SingleSelectType.SEX) {
            titleTv.setText(R.string.setting_sex)
        }
        val mOptionsItems: MutableList<String> = getData()
        wheelView.setTextSize(20F)
        wheelView.adapter = ArrayWheelAdapter(mOptionsItems)
        wheelView.currentItem = curIndex
        cancelTv.setOnClickListener { dismiss() }
        sureTv.setOnClickListener { listener.onSelected(mOptionsItems[curIndex], this@SingleSelectedPop) }
        wheelView.setOnItemSelectedListener { index ->
            curIndex = index
        }
    }

    private fun getData(): MutableList<String> {
        val list = mutableListOf<String>()
        if (type == SingleSelectType.HEIGHT) {
            for (i in 130..210) {
                if (App.userInfo.height == i) {
                    curIndex = i - 130
                }
                list.add("${i}cm")
            }
        } else if (type == SingleSelectType.WEIGHT) {
            for (i in 30..100) {
                if (App.userInfo.weight == i) {
                    curIndex = i - 30
                }
                list.add("${i}kg")
            }
        } else if (type == SingleSelectType.WAIST_LINE) {
            for (i in 40..120) {
                if (App.userInfo.waistline.equals(i.toString())) {
                    curIndex = i - 40
                }
                list.add("${i}cm")
            }
        } else if (type == SingleSelectType.SEX) {
            if (App.userInfo?.sex.equals(context.getString(R.string.setting_sex_man))) {
                curIndex = 0
            } else {
                curIndex = 1
            }
            list.add(context.getString(R.string.setting_sex_man))
            list.add(context.getString(R.string.setting_sex_woman))
        }

        return list
    }

}