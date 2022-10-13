package com.rh.heji.data

/**
 * @date: 2020/11/20
 * @author: 锅得铁
 * #
 */
enum class BillType(private val type: Int, private val text: String) {
    INCOME(+1, "收入"), EXPENDITURE(-1, "支出"), ALL(0, "收支");

    fun type(): Int = type
    fun text(): String = text

    companion object {
        fun transform(type: Int): BillType {
            if (type == INCOME.type()) {
                return INCOME
            }
            return if (type == EXPENDITURE.type()) {
                EXPENDITURE
            } else {
                ALL
            }
        }

        fun transform(text: String): BillType {
            if (text == INCOME.text()) {
                return INCOME
            } else if (text == EXPENDITURE.text()) {
                return EXPENDITURE
            }
            return ALL
        }
    }
}