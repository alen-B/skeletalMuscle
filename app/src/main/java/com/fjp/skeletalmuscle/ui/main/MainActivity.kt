package com.fjp.skeletalmuscle.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.data.model.bean.MainSports
import com.fjp.skeletalmuscle.data.model.bean.SportsType
import com.fjp.skeletalmuscle.databinding.ActivityMainBinding
import com.fjp.skeletalmuscle.ext.dp
import com.fjp.skeletalmuscle.ui.user.adapter.MainSportsRateAdapter
import com.fjp.skeletalmuscle.view.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.viewmodel.state.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    lateinit var mainSportsRateAdapter: MainSportsRateAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mViewModel.sportsData.add(MainSports(SportsType.LEG_LIFT,"88"))
        mViewModel.sportsData.add(MainSports(SportsType.DUMBBELL,"99"))
        mViewModel.sportsData.add(MainSports(SportsType.PUSH_UP,"88"))
        mainSportsRateAdapter = MainSportsRateAdapter(mViewModel.sportsData as ArrayList<MainSports>,0);
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), mainSportsRateAdapter)
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration( 12.dp.toInt(),0))
    }

    inner class ProxyClick{
        fun startSports(){
            showToast("开始运动")
        }
        fun startCalendar(){
           startActivity(Intent(this@MainActivity, CalendarActivity::class.java))
        }
    }
}