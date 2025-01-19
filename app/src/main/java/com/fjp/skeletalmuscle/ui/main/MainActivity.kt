package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.clj.fastble.BleManager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.eventViewModel
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.pop.AssessTipPop
import com.fjp.skeletalmuscle.databinding.ActivityMainBinding
import com.fjp.skeletalmuscle.ui.assessment.SportsAssessmentResultActivity
import com.fjp.skeletalmuscle.ui.main.adapter.ViewPagerFragmentAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsDumbbellFragment
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsHighKneeFragment
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsPlankFragment
import com.fjp.skeletalmuscle.ui.user.adapter.MainSportsRateAdapter
import com.fjp.skeletalmuscle.viewmodel.request.RequestMainViewModel
import com.fjp.skeletalmuscle.viewmodel.state.MainViewModel
import com.lxj.xpopup.XPopup
import com.zhpan.indicator.enums.IndicatorStyle
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.dp2px

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val requestMainViewModel: RequestMainViewModel by viewModels()
    lateinit var mainSportsRateAdapter: MainSportsRateAdapter
    val fragments = mutableListOf<Fragment>()
    private lateinit var viewpagerAdapter: ViewPagerFragmentAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.showSetting.set(true)

        mDatabind.viewpager.pageMargin = dp2px(16)
        mDatabind.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                mDatabind.indicatorView.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mDatabind.indicatorView.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        setMainTitle()
    }

    override fun createObserver() {
        super.createObserver()
        requestMainViewModel.mainLiveData.observe(this) {
            parseState(it, { todayData ->
                mViewModel.showAssess.set(todayData.is_assess == 0)
                findViewById<TextView>(R.id.assessTv).setOnClickListener {
                    showAssessTipPop()
                }
                fragments.clear()
                mViewModel.curScore.set(todayData.score.toString())
                fragments.add(MainSportsHighKneeFragment.newInstance(todayData))
                fragments.add(MainSportsDumbbellFragment.newInstance(todayData))
                fragments.add(MainSportsPlankFragment.newInstance(todayData))
                viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager, 0.95f, fragments)
                mDatabind.viewpager.adapter = viewpagerAdapter
                mDatabind.indicatorView.apply {
                    setSliderColor(getColor(R.color.color_e9e9e9), getColor(R.color.color_blue))
                    setPageSize(viewpagerAdapter.count)
                    setSliderWidth(8f)
                    setSliderHeight(8f)
                    setIndicatorStyle(IndicatorStyle.CIRCLE)
                    notifyDataChanged()
                }
            }, {
                it.printStackTrace()
                showToast(getString(R.string.request_failed))
            })
        }
        requestMainViewModel.userInfoResult.observe(this) {
            parseState(it, { userInfo ->
                CacheUtil.setUser(userInfo)
            })

        }
        eventViewModel.updateAvatarEvent.observeInActivity(this) {
            mViewModel.smAvatar.set(it)
        }
    }

    private fun showAssessTipPop() {

        val assessTipPop = AssessTipPop(this)
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(assessTipPop)
        pop.show()
    }

    override fun onResume() {
        super.onResume()
        requestMainViewModel.getTodayData(DateTimeUtil.formatDate(System.currentTimeMillis(), DateTimeUtil.DATE_PATTERN))
//        requestMainViewModel.getTodayData("2024-12-11")
    }

    private fun setMainTitle() {

        mViewModel.title.set("嘿，您来了，欢迎${App.userInfo?.name}。")
    }


    inner class ProxyClick {
        fun startSportsAssessment() {
            startActivity(Intent(this@MainActivity, SportsAssessmentResultActivity::class.java))
        }

        fun startSports() {
            startActivity(Intent(this@MainActivity, TodaySelectSportsActivity::class.java))
        }

        fun startCalendar() {
            startActivity(Intent(this@MainActivity, CalendarActivity::class.java))
        }

        fun clickTodaySports() {

        }
    }

}