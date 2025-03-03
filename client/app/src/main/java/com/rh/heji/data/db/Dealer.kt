package com.rh.heji.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @date: 2020/9/22
 * @author: 锅得铁
 * #经手人
 */
@Entity(tableName = "dealer")
data class Dealer(@field:ColumnInfo(name = "dealer_name") @field:PrimaryKey var userName: String)