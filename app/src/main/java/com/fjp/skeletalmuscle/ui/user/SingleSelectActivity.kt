package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.databinding.ActivitySingleSelectBinding
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.user.adapter.SingleSelectAdapter
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectViewModel

class SingleSelectActivity : BaseActivity<SingleSelectViewModel, ActivitySingleSelectBinding>() {
    private val HeightDefaultIndex = 35
    private val WeightDefaultIndex = 20
    private val WaistLineDefaultIndex = 20
    private val DayOneWeekDefaultIndex = 3
    private val SupportTtimeDefaultIndex = 2
    private lateinit var singleSelectAdapter: SingleSelectAdapter
    var singleSelectType: Int? = 0
    var currIndex = 0
    var unit = "kg"
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.showRightText.set(true)
        mDatabind.click = ProxyClick()
        singleSelectType = intent?.getIntExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.SUPPORT_TIME.type)
        initData()
        currIndex = getDefaultSelectedIndex()
        singleSelectAdapter = SingleSelectAdapter(mViewModel.dataArr as ArrayList<Int>, unit, currIndex, clickItem = { item, position ->
//            showToast("点击了" + position)
            currIndex = position
        })
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), singleSelectAdapter)


        mDatabind.recyclerView.smoothScrollToPosition(currIndex + 2)
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
            unit = getString(R.string.unit_cm)
            for (i in 0..80) {
                mViewModel.dataArr.add(i, i + 130)
            }
        } else if (singleSelectType == SingleSelectType.WEIGHT.type) {
            mViewModel.curIndex.set("5")
            title = getString(R.string.weight_title)
            unit = getString(R.string.unit_kg)
            for (i in 0..70) {
//                mViewModel.dataArr.add(i, "${i + 30}kg")
                mViewModel.dataArr.add(i, i + 30)
            }

        } else if (singleSelectType == SingleSelectType.WAIST_LINE.type) {
            mViewModel.curIndex.set("6")
            title = getString(R.string.waist_line_title)
            unit = getString(R.string.unit_cm)
            for (i in 0..50) {
                mViewModel.dataArr.add(i, i + 70)
            }
        } else if (singleSelectType == SingleSelectType.DAY_ONE_WEEK.type) {
            mViewModel.curIndex.set("9")
            unit = ""
            title = getString(R.string.day_one_week_title)
            for (i in 0..6) {
                mViewModel.dataArr.add(i, i + 1)
            }

        } else if (singleSelectType == SingleSelectType.SUPPORT_TIME.type) {
            mViewModel.curIndex.set("10")
            title = getString(R.string.sports_time_title)
            unit = ""
            for (i in 0..11) {
                mViewModel.dataArr.add(i, (i + 1) * 10)
            }

        }
        mViewModel.title.set(title)
    }

    inner class ProxyClick {
        fun next() {
            var intent = Intent(this@SingleSelectActivity, SingleSelectActivity::class.java)
            when (singleSelectType) {
                SingleSelectType.HEIGHT.type -> {
                    App.userInfo?.height = mViewModel.dataArr[currIndex]
                    intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.WEIGHT.type)
                }

                SingleSelectType.WEIGHT.type -> {
                    App.userInfo?.weight = mViewModel.dataArr[currIndex]
                    intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.WAIST_LINE.type)
                }

                SingleSelectType.WAIST_LINE.type -> {
                    App.userInfo?.waistline = mViewModel.dataArr[currIndex].toString()
                    intent = Intent(this@SingleSelectActivity, DiseaseActivity::class.java)
                }

                SingleSelectType.DAY_ONE_WEEK.type -> {
                    App.userInfo?.sport_day_nums = mViewModel.dataArr[currIndex]
                    intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.SUPPORT_TIME.type)
                }

                SingleSelectType.SUPPORT_TIME.type -> {
                    App.userInfo?.sport_time = mViewModel.dataArr[currIndex]
                    intent = Intent(this@SingleSelectActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            startActivity(intent)
        }

        fun finish() {
            this@SingleSelectActivity.finish()

        }


    }

    override fun createObserver() {
        super.createObserver()
    }
}