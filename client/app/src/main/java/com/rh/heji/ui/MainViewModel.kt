package com.rh.heji.ui

import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.LogUtils
import com.rh.heji.Config
import com.rh.heji.utils.YearMonth
import java.util.*

/**
 * @date: 2020/11/3
 * @author: 锅得铁
 * # APP运行时 UI常量共享存储
 */
class MainViewModel : ViewModel() {
    init {
        LogUtils.d(
            "MainViewModel",
            "Config enableOfflineMode=${Config.enableOfflineMode}",
            "Config isInitBook=${Config.isInitBook()}",
            "Config isInitUser=${Config.isInitUser()}"
        )
    }
    /**
     * 全局选择的年月（home to subpage）
     */
    var globalYearMonth: YearMonth =
        YearMonth(Calendar.getInstance()[Calendar.YEAR], Calendar.getInstance()[Calendar.MONTH] + 1)

    /**
     * 当前年月
     */
    val currentYearMonth by lazy {
        YearMonth(
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH] + 1
        )
    }
}