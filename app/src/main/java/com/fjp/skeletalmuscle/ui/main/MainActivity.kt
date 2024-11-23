package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.app.weight.pop.NewVersionPop
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityMainBinding
import com.fjp.skeletalmuscle.ui.main.adapter.ViewPagerFragmentAdapter
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsDumbbellFragment
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsHighKneeFragment
import com.fjp.skeletalmuscle.ui.main.fragment.MainSportsPlankFragment
import com.fjp.skeletalmuscle.ui.user.adapter.MainSportsRateAdapter
import com.fjp.skeletalmuscle.viewmodel.state.MainViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SEX
import com.lxj.xpopup.XPopup
import com.zhpan.indicator.enums.IndicatorStyle
import me.hgj.jetpackmvvm.ext.util.dp2px
import java.time.LocalDateTime

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    lateinit var mainSportsRateAdapter: MainSportsRateAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        CacheUtil.setUser(App.userInfo)
        mDatabind.click = ProxyClick()
        mViewModel.showSetting.set(true)

        val fragments =arrayListOf<Fragment>(MainSportsHighKneeFragment.newInstance(), MainSportsDumbbellFragment.newInstance(),MainSportsPlankFragment.newInstance())
        val viewpagerAdapter = ViewPagerFragmentAdapter(supportFragmentManager,0.93f,fragments)
        mDatabind.viewpager.adapter = viewpagerAdapter
        mDatabind.indicatorView.apply {
            setSliderColor(getColor(R.color.color_e9e9e9),getColor(R.color.color_blue))
            setPageSize(viewpagerAdapter.count)
            setSliderWidth(8f)
            setSliderHeight(8f)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            notifyDataChanged()
        }
        mDatabind.viewpager.pageMargin=dp2px(16)
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

    override fun onResume() {
        super.onResume()
        val sports =CacheUtil.getSports()
        sports?.let {
            val curDay = DatetimeUtil.getCurDate2Str()
            val todaySports = it.sports[curDay]
            todaySports?.let {highKneeSports ->
                mViewModel.sportsData.clear()
                mViewModel.sportsData.add(MainSports(SportsType.HIGH_KNEE,highKneeSports))
//                mViewModel.sportsData.add(MainSports(SportsType.DUMBBELL,highKneeSports))
//                mViewModel.sportsData.add(MainSports(SportsType.PLANK,highKneeSports))
//                mainSportsRateAdapter = MainSportsRateAdapter(mViewModel.sportsData as ArrayList<MainSports>,0)
//                mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), mainSportsRateAdapter)
//                mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration( 12.dp.toInt(),0))
                mViewModel.sportsTime.set(DatetimeUtil.formSportTime(highKneeSports.time))
                mViewModel.curScore.set(highKneeSports.score.toString())
                mViewModel.heat.set(highKneeSports.calories.toString())
                mViewModel.endurance.set("0.32")
                mViewModel.heartRate.set(highKneeSports.minHeartRate.toString()+"-"+highKneeSports.maxHeartRate.toString())
            }

        }
    }
    private fun setMainTitle() {
        val now = LocalDateTime.now()
        val hour = now.hour

        val timeOfDay = if (hour < 12) "上午好" else "下午好"
        if(App.userInfo?.sex == SEX.WOMAN.value){
            mViewModel.title.set("${App.userInfo?.name}奶奶，${timeOfDay}")
        }else{
            mViewModel.title.set("${App.userInfo?.name}爷爷，${timeOfDay}")
        }
    }
    private fun showNewVersionPop() {
        val changeAccountPop = NewVersionPop(this, object : NewVersionPop.Listener {


            override fun onClickUpdate(pop: NewVersionPop) {
                //TODO 执行版本更新
            }


        })
        val pop = XPopup.Builder(this)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .isDestroyOnDismiss(true).autoOpenSoftInput(false)
            .asCustom(changeAccountPop)

        pop.show()

    }
    inner class ProxyClick{
        fun startSportsRvaluation(){
            startActivity(Intent(this@MainActivity,TodaySelectSportsActivity::class.java))
        }
        fun startSports(){
            startActivity(Intent(this@MainActivity,TodaySelectSportsActivity::class.java))
        }
        fun startCalendar(){
           startActivity(Intent(this@MainActivity, CalendarActivity::class.java))
        }
        fun clickTodaySports(){

        }
    }

}