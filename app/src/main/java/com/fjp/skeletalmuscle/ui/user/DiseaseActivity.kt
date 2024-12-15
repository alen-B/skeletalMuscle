package com.fjp.skeletalmuscle.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.App
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.app.ext.showToast
import com.fjp.skeletalmuscle.data.model.bean.Disease
import com.fjp.skeletalmuscle.databinding.ActivityDiseaseBinding
import com.fjp.skeletalmuscle.ui.user.adapter.DiseaseAdapter
import com.fjp.skeletalmuscle.ui.user.adapter.DiseaseDetailAdapter
import com.fjp.skeletalmuscle.viewmodel.state.DiseaseViewModel

class DiseaseActivity : BaseActivity<DiseaseViewModel, ActivityDiseaseBinding>(), (String, Int) -> Unit {

    lateinit var diseaseAdapter: DiseaseAdapter
    lateinit var diseaseDetailAdapter: DiseaseDetailAdapter
    var curDiseaseName = "无"
    var curDiseaseDetailName = ""
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mViewModel.title.set(getString(R.string.disease_title))
        mDatabind.click = ProxyClick()
        mViewModel.curIndex.set("7")
        mViewModel.totalIndex.set("/10")
        mViewModel.showRightText.set(true)
        diseaseAdapter = DiseaseAdapter(mViewModel.dataArr as ArrayList<String>, 0, this)
        diseaseDetailAdapter = DiseaseDetailAdapter(arrayListOf(), 0, clickItem = { item, position ->
//            showToast("点击了" + position)
            curDiseaseDetailName = item
        })
        mDatabind.recyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), diseaseAdapter)
        mDatabind.detailRecyclerView.init(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false), diseaseDetailAdapter)

    }


    inner class ProxyClick {
        fun next() {
            val diseases = arrayListOf<Disease>()
            if (curDiseaseName == "其他") {
                if (mViewModel.other.get().toString().isEmpty()) {
                    showToast(getString(R.string.disease_please_input_disease))
                    return
                }
                val disease = Disease(disease_name = curDiseaseName, child_disease_name = arrayListOf(mViewModel.other.get().toString()))
                diseases.add(disease)
            } else if (curDiseaseName != "无") {
                if (curDiseaseDetailName.isEmpty()) {
                    showToast(getString(R.string.disease_please_selected_detail))
                    return
                }
                val disease = Disease(disease_name = curDiseaseName, child_disease_name = arrayListOf(curDiseaseDetailName))
                diseases.add(disease)
            }
            App.userInfo.disease = diseases

            startActivity(Intent(this@DiseaseActivity, SportsActivity::class.java))
        }

        fun finish() {
            this@DiseaseActivity.finish()

        }


    }

    override fun invoke(item: String, position: Int) {
        curDiseaseName = item
        curDiseaseDetailName = ""
        diseaseDetailAdapter.defaultIndex = -1
        when (position) {
            0 -> {
                mDatabind.detailRecyclerView.visibility = View.GONE
                mDatabind.otherEt.visibility = View.GONE
            }

            1 -> {
                diseaseDetailAdapter.setList(mViewModel.heartDisease)
                mDatabind.detailRecyclerView.visibility = View.VISIBLE
                mDatabind.otherEt.visibility = View.GONE
            }

            2 -> {
                diseaseDetailAdapter.setList(mViewModel.lungDisease)
                mDatabind.detailRecyclerView.visibility = View.VISIBLE
                mDatabind.otherEt.visibility = View.GONE
            }

            3 -> {
                diseaseDetailAdapter.setList(mViewModel.jointDisease)
                mDatabind.detailRecyclerView.visibility = View.VISIBLE
                mDatabind.otherEt.visibility = View.GONE
            }

            4 -> {
                mDatabind.detailRecyclerView.visibility = View.GONE
                mDatabind.otherEt.visibility = View.VISIBLE
            }

        }
    }

    override fun createObserver() {
        super.createObserver()
    }
}