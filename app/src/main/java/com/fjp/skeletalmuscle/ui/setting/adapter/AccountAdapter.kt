package com.fjp.skeletalmuscle.ui.setting.adapter

import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fjp.skeletalmuscle.R
import com.fjp.skeletalmuscle.app.ext.setAdapterAnimation
import com.fjp.skeletalmuscle.app.util.SettingUtil
import com.fjp.skeletalmuscle.app.weight.CircleImageView
import com.fjp.skeletalmuscle.data.model.bean.Account


class AccountAdapter(data: MutableList<Account>, var clickItem: (item: Account, position: Int) -> Unit) : BaseQuickAdapter<Account, BaseViewHolder>(R.layout.item_account, data) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    override fun convert(holder: BaseViewHolder, item: Account) {
        holder.setText(R.id.nameTv, item.name)
        if (holder.bindingAdapterPosition == 0) {
            holder.setIsRecyclable(false)
            holder.setBackgroundResource(R.id.avatarIv, R.drawable.account_add)
        } else {
            holder.getView<CircleImageView>(R.id.avatarIv).load(item.avatar, builder = {
                error(R.drawable.avatar_default)
                allowHardware(false)
                placeholder(R.drawable.avatar_default)
            })
        }
        holder.itemView.setOnClickListener {
            clickItem.invoke(item, holder.bindingAdapterPosition)
        }
    }
}


