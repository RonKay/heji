package com.rh.heji.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 *
 * @date 2023/4/25
 * @author 锅得铁
 * @since v1.0
 */
@Dao
interface BookUSerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookUser: BookUser)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(category: Category)

    @Query("select count(0) from book_user")
    fun count(): Int

}