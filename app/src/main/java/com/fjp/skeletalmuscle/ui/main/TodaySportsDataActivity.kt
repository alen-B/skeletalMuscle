package com.fjp.skeletalmuscle.ui.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.databinding.ActivityTodaySportsDataBinding
import com.fjp.skeletalmuscle.ext.dp
import com.fjp.skeletalmuscle.ui.main.adapter.TodaySportsDataAdapter
import com.fjp.skeletalmuscle.view.recyclerview.SpaceItemDecoration
import com.fjp.skeletalmuscle.viewmodel.state.TodaySportsDataViewModel

class TodaySportsDataActivity :BaseActivity<TodaySportsDataViewModel,ActivityTodaySportsDataBinding>() {
    lateinit var todaySportsDataAdapter:TodaySportsDataAdapter
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        todaySportsDataAdapter = TodaySportsDataAdapter(mViewModel.dataArr as ArrayList<String>, clickItem = { item ->
            showToast("点击了")
        })
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), todaySportsDataAdapter)
        mDatabind.recyclerView.addItemDecoration(SpaceItemDecoration( 16.dp.toInt(),0))
    }


    inner class ProxyClick{
        fun clickShare(){

        }
        fun clickSportsRecord(){

        }
    }
}