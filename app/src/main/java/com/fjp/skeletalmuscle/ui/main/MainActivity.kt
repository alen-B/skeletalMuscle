package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.DateTimeUtil
import com.fjp.skeletalmuscle.app.weight.pop.NewVersionPop
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
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
import java.time.LocalDateTime

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    private val requestMainViewModel: RequestMainViewModel by viewModels()
    lateinit var mainSportsRateAdapter: MainSportsRateAdapter
    val fragments = mutableListOf<Fragment>()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.showSetting.set(true)


        val viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager, 0.93f, fragments)
        mDatabind.viewpager.adapter = viewpagerAdapter
        mDatabind.indicatorView.apply {
            setSliderColor(getColor(R.color.color_e9e9e9), getColor(R.color.color_blue))
            setPageSize(viewpagerAdapter.count)
            setSliderWidth(8f)
            setSliderHeight(8f)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            notifyDataChanged()
        }
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
//        showNewVersionPop()
    }


    override fun createObserver() {
        super.createObserver()
        requestMainViewModel.mainLiveData.observe(this) {
            parseState(it, {
                mViewModel.curScore.set(it.score.toString())
                if (it.sport_lift_leg!=null) {
                    fragments.add(MainSportsHighKneeFragment.newInstance(it))
                }
                if (it.sport_dumbbell!=null) {
                    fragments.add(MainSportsDumbbellFragment.newInstance(it))
                }
                if (it.sport_flat_support!=null) {
                    fragments.add(MainSportsPlankFragment.newInstance(it))
                }
            }, {
                it.printStackTrace()
                showToast(getString(R.string.request_failed))
            })
        }
    }

    override fun onResume() {
        super.onResume()
        requestMainViewModel.getTodayData()
        val sports = CacheUtil.getSports()
        sports?.let {
            val curDay = DateTimeUtil.getCurDate2Str()
            val todaySports = it.sports[curDay]
            todaySports?.let { highKneeSports ->
                mViewModel.sportsData.clear()
                mViewModel.sportsData.add(MainSports(SportsType.HIGH_KNEE, highKneeSports))
//                mViewModel.sportsData.add(MainSports(SportsType.DUMBBELL,highKneeSports))
//                mViewModel.sportsData.add(MainSports(SportsType.PLANK,highKneeSports))
//                mainSportsRateAdapter = MainSportsRateAdapter(mViewModel.sportsData as ArrayList<MainSports>,0)
//                mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), mainSportsRateAdapter)
//                mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration( 12.dp.toInt(),0))
                mViewModel.sportsTime.set(DateTimeUtil.formSportTime(highKneeSports.time))
                mViewModel.curScore.set(highKneeSports.score.toString())
                mViewModel.heat.set(highKneeSports.calories.toString())
                mViewModel.endurance.set("0.32")
                mViewModel.heartRate.set(highKneeSports.minHeartRate.toString() + "-" + highKneeSports.maxHeartRate.toString())
            }

        }
    }

    private fun setMainTitle() {
        val now = LocalDateTime.now()
        val hour = now.hour

        val timeOfDay = if (hour < 12) "上午好" else "下午好"
        if (App.userInfo?.sex == getString(R.string.setting_sex_woman)) {
            mViewModel.title.set("${App.userInfo?.name}奶奶，${timeOfDay}")
        } else {
            mViewModel.title.set("${App.userInfo?.name}爷爷，${timeOfDay}")
        }
    }

    private fun showNewVersionPop() {
        val changeAccountPop = NewVersionPop(this, object : NewVersionPop.Listener {


            override fun onClickUpdate(pop: NewVersionPop) {
                //TODO 执行版本更新
            }


        })
        val pop = XPopup.Builder(this).dismissOnTouchOutside(true).dismissOnBackPressed(true).isDestroyOnDismiss(true).autoOpenSoftInput(false).asCustom(changeAccountPop)

        pop.show()

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