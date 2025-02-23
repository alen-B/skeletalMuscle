package com.fjp.skeletalmuscle.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import coil.load
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.Constants
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.data.model.bean.result.TodayDataResult
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDataBinding
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDataAdapter
import com.fjp.skeletalmuscle.ui.main.adapter.ViewPagerFragmentAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsDumbbellFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsHighKneeFragment
import com.fjp.skeletalmuscle.ui.main.fragment.TodaySportsPlankFragment
import com.fjp.skeletalmuscle.viewmodel.state.ShareViewModel
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDataViewModel
import com.zhpan.indicator.enums.IndicatorStyle
import me.hgj.jetpackmvvm.ext.util.dp2px
import me.hgj.jetpackmvvm.util.get

class TodaySportsActivity : BaseActivity<TodaySportsDataViewModel, ActivityTodaySportsDataBinding>() {
    val fragments = arrayListOf<Fragment>()
    val shareViewmodel  : ShareViewModel by viewModels()
    companion object {
        fun start(context: Context, todayDataResult: TodayDataResult, sportsType: SportsType) {
            val intent = Intent(context, TodaySportsActivity::class.java)
            intent.putExtra(Constants.INTENT_KEY_TODAY_SPORTS_DATA, todayDataResult)
            intent.putExtra(Constants.INTENT_KEY_TODAY_SPORTS_TYPE, sportsType.type)
            context.startActivity(intent)
        }
    }

    lateinit var todaySportsDataAdapter: TodaySportsDataAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.today_sports_data_title))
        mDatabind.click = ProxyClick()
        //type用来记录是点击那个运动进来的详情
        val type = intent.get(Constants.INTENT_KEY_TODAY_SPORTS_TYPE, SportsType.HIGH_KNEE.type)
        val todaySportsData = intent.getParcelableExtra<TodayDataResult>(Constants.INTENT_KEY_TODAY_SPORTS_DATA)

        if(type == SportsType.HIGH_KNEE.type){
            if (todaySportsData?.sport_lift_leg != null) {
                fragments.add(TodaySportsHighKneeFragment.newInstance(todaySportsData?.sport_lift_leg))
            }
        }else if(type == SportsType.DUMBBELL.type){
            if (todaySportsData?.sport_dumbbell != null ) {
                fragments.add(TodaySportsDumbbellFragment.newInstance(todaySportsData?.sport_dumbbell))
            }
        }else if(type == SportsType.PLANK.type){
            if (todaySportsData?.sport_flat_support != null ) {
                fragments.add(TodaySportsPlankFragment.newInstance(todaySportsData?.sport_flat_support))
            }
        }
        val viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager, 1f, fragments)
        mDatabind.viewpager.adapter = viewpagerAdapter
    }

    override fun onResume() {
        super.onResume()
        mDatabind.blurLayout.startBlur()
    }

    override fun onPause() {
        super.onPause()
        mDatabind.blurLayout.pauseBlur()
    }

    inner class ProxyClick {
        fun clickShare() {
            val shareTitleView = View.inflate(this@TodaySportsActivity, R.layout.share_title, null)
            val shareTBottomView = View.inflate(this@TodaySportsActivity, R.layout.share_bottom, null)
            val avatarIv = shareTitleView.findViewById<CircleImageView>(R.id.avatarIv)
            val nameTv = shareTitleView.findViewById<TextView>(R.id.nameTv)
            val timeTv = shareTitleView.findViewById<TextView>(R.id.timeTv)
            nameTv.text = App.userInfo.name
            timeTv.text = DateTimeUtil.formatShareTime(System.currentTimeMillis())
            avatarIv.load(App.userInfo.profile)
            avatarIv.load(App.userInfo.profile, builder = {
                allowHardware(false)
                this.error(R.drawable.avatar_default)
                this.placeholder(R.drawable.avatar_default)
            })
            fragments[0].view?.let {
                shareViewmodel.share(this@TodaySportsActivity,shareTitleView,it,shareTBottomView)
            }



        }

        fun clickSportsRecord() {
            startActivity(Intent(this@TodaySportsActivity, CalendarActivity::class.java))
        }

        fun finish() {
            this@TodaySportsActivity.finish()
        }
    }
}