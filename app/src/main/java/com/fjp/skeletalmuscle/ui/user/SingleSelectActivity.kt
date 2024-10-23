package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.databinding.ActivitySingleSelectBinding
import com.fjp.skeletalmuscle.ui.user.adapter.SingleSelectAdapter
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectViewModel

class SingleSelectActivity : BaseActivity<SingleSelectViewModel, ActivitySingleSelectBinding>() {
    val HeightDefaultIndex = 35
    val WeightDefaultIndex = 20
    val WaistLineDefaultIndex = 20
    val DayOneWeekDefaultIndex = 3
    val SupportTtimeDefaultIndex = 2
    lateinit var singleSelectAdapter: SingleSelectAdapter
    var singleSelectType: Int? = 0
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.showRightText.set(true)
        mDatabind.click = ProxyClick()
        singleSelectType = intent?.getIntExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.SUPPORT_TIME.type)
        initData()
        val defaultSelectedIndex = getDefaultSelectedIndex()
        singleSelectAdapter = SingleSelectAdapter(mViewModel.dataArr as ArrayList<String>, defaultSelectedIndex, clickItem = { item, position ->
            showToast("点击了" + position)
        })
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), singleSelectAdapter)


        mDatabind.recyclerView.smoothScrollToPosition(defaultSelectedIndex + 2)
    }

    private fun getDefaultSelectedIndex(): Int {
        return when (singleSelectType) {
            SingleSelectType.HEIGHT.type -> HeightDefaultIndex
            SingleSelectType.WEIGHT.type -> WeightDefaultIndex
            SingleSelectType.WAIST_LINE.type -> WaistLineDefaultIndex
            SingleSelectType.DAY_ONE_WEEK.type -> DayOneWeekDefaultIndex
            else -> {
                SupportTtimeDefaultIndex
            }
        }
    }

    private fun initData() {
        var title = getString(R.string.height_title)
        mViewModel.totalIndex.set("/10")
        if (singleSelectType == SingleSelectType.HEIGHT.type) {
            mViewModel.curIndex.set("4")

            for (i in 0..80) {
                mViewModel.dataArr.add(i, "${i + 130}cm")
            }
        } else if (singleSelectType == SingleSelectType.WEIGHT.type) {
            mViewModel.curIndex.set("5")
            title = getString(R.string.weight_title)
            for (i in 0..70) {
                mViewModel.dataArr.add(i, "${i + 30}kg")
            }

        } else if (singleSelectType == SingleSelectType.WAIST_LINE.type) {
            mViewModel.curIndex.set("6")
            title = getString(R.string.waist_line_title)
            for (i in 0..50) {
                mViewModel.dataArr.add(i, "${i + 70}cm")
            }
        } else if (singleSelectType == SingleSelectType.DAY_ONE_WEEK.type) {
            mViewModel.curIndex.set("9")
            mDatabind.laterBtn.visibility = View.VISIBLE
            title = getString(R.string.day_one_week_title)
            for (i in 0..6) {
                mViewModel.dataArr.add(i, "${i + 1}天")
            }

        } else if (singleSelectType == SingleSelectType.SUPPORT_TIME.type) {
            mViewModel.curIndex.set("10")
            mDatabind.laterBtn.visibility = View.VISIBLE
            title = getString(R.string.sports_time_title)
            for (i in 0..11) {
                mViewModel.dataArr.add(i, "${(i + 1) * 10}分钟")
            }

        }
        mViewModel.title.set(title)
    }

    inner class ProxyClick {
        fun next() {
            var intent = Intent(this@SingleSelectActivity, SingleSelectActivity::class.java)
            when (singleSelectType) {
                SingleSelectType.HEIGHT.type -> intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.WEIGHT.type)
                SingleSelectType.WEIGHT.type -> intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.WAIST_LINE.type)
                SingleSelectType.WAIST_LINE.type -> {
                    intent = Intent(this@SingleSelectActivity, DiseaseActivity::class.java)
                }

                SingleSelectType.DAY_ONE_WEEK.type -> intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.SUPPORT_TIME.type)
            }
            startActivity(intent)
        }

        fun finish() {
            this@SingleSelectActivity.finish()

        }


    }
}