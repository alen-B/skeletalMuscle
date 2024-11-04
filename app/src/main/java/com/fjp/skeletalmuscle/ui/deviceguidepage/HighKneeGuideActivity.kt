package com.fjp.skeletalmuscle.ui.deviceguidepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.fjp.skeletalmuscle.app.base.BaseActivity
import com.fjp.skeletalmuscle.app.ext.init
import com.fjp.skeletalmuscle.databinding.ActivityHighKneeGuideBinding
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep10Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep11Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep1Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep2Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep3Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep4Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep5Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep6Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep7Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep8Fragment
import com.fjp.skeletalmuscle.ui.deviceguidepage.fragment.HighKneeGuideStep9Fragment
import com.fjp.skeletalmuscle.viewmodel.state.HighKneeGuideViewModel

class HighKneeGuideActivity : BaseActivity<HighKneeGuideViewModel, ActivityHighKneeGuideBinding>() {
    private val fragments = arrayListOf<Fragment>(HighKneeGuideStep1Fragment.newInstance()
        , HighKneeGuideStep2Fragment.newInstance(),
        HighKneeGuideStep3Fragment.newInstance(),
        HighKneeGuideStep4Fragment.newInstance(),
        HighKneeGuideStep5Fragment.newInstance(),
        HighKneeGuideStep6Fragment.newInstance(),
        HighKneeGuideStep7Fragment.newInstance(),
        HighKneeGuideStep8Fragment.newInstance(),
        HighKneeGuideStep9Fragment.newInstance(),
        HighKneeGuideStep10Fragment.newInstance(),
        HighKneeGuideStep11Fragment.newInstance())
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewModel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.viewpager.init(supportFragmentManager, lifecycle, fragments, false)
    }

    inner class ProxyClick {
        fun clickFinish() {
            this@HighKneeGuideActivity.finish()
        }

        fun clickNext() {
            if (mDatabind.viewpager.currentItem == mDatabind.viewpager.adapter!!.itemCount-1) {
                return
            }
            mDatabind.viewpager.setCurrentItem(mDatabind.viewpager.currentItem + 1, true)
        }

        fun clickPre() {
            if (mDatabind.viewpager.currentItem == 0) {
                return
            }
            mDatabind.viewpager.setCurrentItem(mDatabind.viewpager.currentItem - 1, true)
        }
    }
}