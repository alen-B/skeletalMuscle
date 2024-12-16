package com.fjp.skeletalmuscle.app.util

import android.text.TextUtils
import com.fjp.skeletalmuscle.data.model.bean.Account
import com.fjp.skeletalmuscle.data.model.bean.TodayhignKneeSports
import com.fjp.skeletalmuscle.data.model.bean.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

object CacheUtil {
    /**
     * 获取保存的账户信息
     */
    fun getUser(): UserInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userInfo: UserInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userInfo == null) {
            kv.encode("user", "")
            setIsLogin(false)
        } else {
            kv.encode("user", Gson().toJson(userInfo))
            setIsLogin(true)
        }
    }

    fun setSports(highKneeSports: TodayhignKneeSports) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("todayHighKneeSports", Gson().toJson(highKneeSports))

    }

    //用来记录切换账号时，显示的账号
    fun setAccounts(accounts: MutableList<Account>) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("accounts", Gson().toJson(accounts))
    }

    fun getAccounts(): MutableList<Account> {
        val kv = MMKV.mmkvWithID("app")
        val accountsJSON = kv.decodeString("accounts")
        if (accountsJSON != null) {
            return Gson().fromJson(accountsJSON, object : TypeToken<MutableList<Account>>() {}.type)
        } else {
            return mutableListOf()
        }
    }

    fun removeAccount(phone: String) {
        val kv = MMKV.mmkvWithID("app")
        val accountsJSON = kv.decodeString("accounts")
        if (accountsJSON != null) {
            val accounts: MutableList<Account> = Gson().fromJson(accountsJSON, object : TypeToken<MutableList<Account>>() {}.type)
            accounts.forEach {
                if (it.phone == phone) {
                    accounts.remove(it)
                }
            }
        }
    }


    fun getSports(): TodayhignKneeSports? {
        val kv = MMKV.mmkvWithID("app")
        val highKneeSports = kv.decodeString("todayHighKneeSports")
        return if (TextUtils.isEmpty(highKneeSports)) {
            return null
        } else {
            return Gson().fromJson(highKneeSports, TodayhignKneeSports::class.java)
        }

    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }

    fun setVoiceInteraction(isOpen: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("voiceInteraction", isOpen)
    }

    fun getVoiceInteraction(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("voiceInteraction", true)
    }

}