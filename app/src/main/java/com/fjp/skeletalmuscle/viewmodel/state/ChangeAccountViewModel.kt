package com.fjp.skeletalmuscle.viewmodel.state

import com.fjp.skeletalmuscle.app.base.SMBaseViewModel
import com.fjp.skeletalmuscle.data.model.bean.Account

/**
 *Author:Mr'x
 *Time:2024/11/21
 *Description:
 */
class ChangeAccountViewModel:SMBaseViewModel() {
    val accounts = mutableListOf<Account>()
    init {
        accounts.add(Account("冯大爷","15652631379",""))
        accounts.add(Account("张大妈","15688888888",""))
        accounts.add(Account("李大妈","15666666666",""))
        accounts.add(Account("添加账号","",""))
    }
}