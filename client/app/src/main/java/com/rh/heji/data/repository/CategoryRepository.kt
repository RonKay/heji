package com.rh.heji.data.repository

import android.text.TextUtils
import com.rh.heji.App
import com.rh.heji.data.BillType
import com.rh.heji.data.DataRepository
import com.rh.heji.data.Result
import com.rh.heji.data.db.Category
import com.rh.heji.data.db.STATUS
import com.rh.heji.network.BaseResponse
import com.rh.heji.network.request.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepository : DataRepository() {
    suspend fun addCategory(category: CategoryEntity, bookId: String) {
        val response = network.categoryPush(category)
        response.let {
            val dbCategory = category.toDbCategory()
            dbCategory.syncStatus = STATUS.SYNCED
            categoryDao.update(dbCategory)
        }
    }

    suspend fun deleteCategory(_id: String): Flow<Result<Boolean>> {
        val response = network.categoryDelete(_id)
        response.let {
            categoryDao.deleteById(_id)
        }
        return flow { emit(Result.Success(true)) }
    }

    suspend fun getCategory() {
        val response: BaseResponse<List<CategoryEntity>> = network.categoryPull()
        val categories = response.data
        if (categories.isNotEmpty()) {
            categories.forEach { entity: CategoryEntity ->
                val _id = App.dataBase.categoryDao().findByID(entity.id)
                if (TextUtils.isEmpty(_id)) {
                    val dbCategory = entity.toDbCategory()
                    dbCategory.syncStatus = STATUS.SYNCED
                    App.dataBase.categoryDao().insert(dbCategory)
                }
            }
        }
    }

    suspend fun updateCategory(category: Category) {
        category.syncStatus = STATUS.UPDATED
        categoryDao.update(category)
        val response = network.categoryUpdate()
        if (response.code == OK) {
            category.syncStatus = STATUS.SYNCED
            categoryDao.update(category)
        }
    }
}