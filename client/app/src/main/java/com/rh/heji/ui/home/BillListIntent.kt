package com.rh.heji.ui.home

import com.chad.library.adapter.base.entity.node.BaseNode
import com.rh.heji.data.db.Image
import com.rh.heji.data.db.dto.Income
import com.rh.heji.ui.base.IAction
import com.rh.heji.ui.base.IUiState
import com.rh.heji.utils.YearMonth

/**
 *
 * @date 2022/10/1
 * @author 锅得铁
 * @since v1.0
 */
internal sealed interface BillListUiState : IUiState {
    class Bills(val nodeList: MutableList<BaseNode>) : BillListUiState
    class Summary(val income: Income) : BillListUiState
    class Error(val t: Throwable) : BillListUiState
    class Images(val data: MutableList<Image>):BillListUiState
}

internal sealed class BillListAction : IAction {
    object Refresh : BillListAction()
    class Summary(val yearMonth: YearMonth) : BillListAction()
    class MonthBill(val yearMonth: YearMonth) : BillListAction()
    class GetImages(val bid: String) : BillListAction()
//    class YearBill(val year: String) : BillListAction()
}