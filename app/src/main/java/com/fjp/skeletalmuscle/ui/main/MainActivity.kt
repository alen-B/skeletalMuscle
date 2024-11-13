package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.util.CacheUtil
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityMainBinding
import com.fjp.skeletalmuscle.app.ext.dp
import com.fjp.skeletalmuscle.app.util.DatetimeUtil
import com.fjp.skeletalmuscle.ui.user.adapter.MainSportsRateAdapter
import com.fjp.skeletalmuscle.app.weight.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.ui.setting.SettingActivity
import com.fjp.skeletalmuscle.viewmodel.state.MainViewModel
import com.fjp.skeletalmuscle.viewmodel.state.SEX
import java.time.LocalDateTime

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    lateinit var mainSportsRateAdapter: MainSportsRateAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        CacheUtil.setUser(App.userInfo)
        mDatabind.click = ProxyClick()
        mViewModel.showSetting.set(true)
            findViewById<ImageView>(R.id.settingIv).setOnClickListener {
                startActivity(Intent(this@MainActivity,SettingActivity::class.java))
            }
//        mViewModel.sportsData.add(MainSports(SportsType.DUMBBELL,"99"))
//        mViewModel.sportsData.add(MainSports(SportsType.PLANK,"88"))
        mainSportsRateAdapter = MainSportsRateAdapter(mViewModel.sportsData as ArrayList<MainSports>,0)
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), mainSportsRateAdapter)
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration( 12.dp.toInt(),0))
        setMainTitle()
    }

    override fun onResume() {
        super.onResume()
        val sports =CacheUtil.getSports()
        sports?.let {
            val curDay = DatetimeUtil.getCurDate2Str()
            val todaySports = it.sports[curDay]
            todaySports?.let {highKneeSports ->
                mViewModel.sportsData.add(MainSports(SportsType.HIGH_KNEE,highKneeSports))
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

    inner class ProxyClick{
        fun startSports(){
            startActivity(Intent(this@MainActivity,TodaySelectSportsActivity::class.java))
        }
        fun startCalendar(){
           startActivity(Intent(this@MainActivity, CalendarActivity::class.java))
        }
        fun clickTodaySports(){
            startActivity(Intent(this@MainActivity,TodaySportsActivity::class.java))
        }
    }

}