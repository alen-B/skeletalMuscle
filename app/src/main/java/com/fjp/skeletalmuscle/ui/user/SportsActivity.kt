package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.common.Constants
import com.fjp.skeletalmuscle.data.model.bean.Sports
import com.fjp.skeletalmuscle.databinding.ActivitySuportsBinding
import com.fjp.skeletalmuscle.ui.main.MainActivity
import com.fjp.skeletalmuscle.ui.user.adapter.SportsTypeAdapter
import com.fjp.skeletalmuscle.view.pop.AddSportsTypePop
import com.fjp.skeletalmuscle.viewmodel.state.SingleSelectType
import com.fjp.skeletalmuscle.viewmodel.state.SuportsViewModel
import com.lxj.xpopup.XPopup
import me.hgj.jetpackmvvm.base.appContext

class SportsActivity : BaseActivity<SuportsViewModel, ActivitySuportsBinding>() {

    lateinit var suportsAdapter: SportsTypeAdapter
    var curItem:Sports? = null
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.sports_type_title))
        mDatabind.click = ProxyClick()
        mViewModel.curIndex.set("8")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        suportsAdapter = SportsTypeAdapter(mViewModel.dataArr as ArrayList<Sports>, clickItem = { item ->
            curItem = item
        })
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), suportsAdapter)


    }

    inner class ProxyClick {
        fun next() {
            if(curItem?.name === appContext.getString(R.string.sports_type_no)){
                App.eventViewModelInstance.finish.value=true
                val intent =  Intent(this@SportsActivity,MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent =  Intent(this@SportsActivity,SingleSelectActivity::class.java)
                intent.putExtra(Constants.INTENT_KEY_SINGLESELECT_TYPE, SingleSelectType.DAY_ONE_WEEK.type)
                startActivity(intent)
            }

        }

        fun finish() {
            this@SportsActivity.finish()
        }

        fun addSportsType() {
            val pop = XPopup.Builder(this@SportsActivity)
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .isDestroyOnDismiss(true)
                .autoOpenSoftInput(true)
                .asCustom(AddSportsTypePop(this@SportsActivity,object: AddSportsTypePop.InputPasswordListener{
                    override fun input(name: String) {
                        if(name.isEmpty()){
                            showToast("请输入类型名称")
                            return
                        }
                        suportsAdapter.addData(0, Sports(name,true))
                        mDatabind.recyclerView.smoothScrollToPosition(0)
                    }

                }))

            pop.show()
        }


    }
    override fun createObserver() {
        super.createObserver()
        App.eventViewModelInstance.finish.observeInActivity(this) {
            finish()
        }
    }
}