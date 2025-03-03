package com.rh.heji

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import com.rh.heji.data.AppDatabase
import com.rh.heji.service.sync.SyncService

/**
 * @date: 2020/8/28
 * @author: 锅得铁
 * # 启动初始化顺序：
 *  1.AppInitializer
 *  2.Application
 *  3.AppViewModule
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        viewModel = AppViewModel(this)
        context = this
        init()
    }

    private fun init() {
        Intent(this, SyncService::class.java).also {
            startService(it)
        }
        if (Config.isInitUser())
            switchDataBase(Config.user.name)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var viewModel: AppViewModel
            private set

        /**
         * 数据库在登录后初始化不同用户创建不同数据库（username_data）
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var dataBase: AppDatabase
            private set

        @JvmName("switchDataBase")
        fun switchDataBase(userName: String) {
            //选择数据库时关闭上一个数据库连接并重建
            if (this::dataBase.isInitialized)
                dataBase.reset()
            dataBase = AppDatabase.getInstance(userName)
        }
    }
}
