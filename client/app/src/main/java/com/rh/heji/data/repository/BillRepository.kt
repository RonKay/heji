package com.rh.heji.data.repository

import com.blankj.utilcode.util.LogUtils
import com.rh.heji.App
import com.rh.heji.FILE_LENGTH_1M
import com.rh.heji.data.DataRepository
import com.rh.heji.data.db.Bill
import com.rh.heji.data.db.STATUS
import com.rh.heji.network.BaseResponse
import com.rh.heji.network.response.ImageEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import top.zibin.luban.Luban
import java.io.File

class BillRepository : DataRepository() {

    suspend fun deleteBill(_id: String) {
        var response = network.billDelete(_id)
        response.data.let {
            App.dataBase.imageDao().deleteBillImage(_id)
            billDao.delete(Bill(_id))
        }
    }

    suspend fun updateBill(bill: Bill) {
        var response = network.billUpdate(bill)
        response.data.let {
            bill.syncStatus = STATUS.SYNCED
            billDao.update(bill) //已上传
        }
    }

    suspend fun pullBill(startTime: String = "0", endTime: String = "0") {
        var response = network.billPull(startTime, endTime)
        response.data.let {
            if (it.isNotEmpty()) {
                it.forEach { bill ->
                    billDao.install(bill)
                }
            }
        }
    }

    /**
     * 上传账单图片
     */
    private suspend fun uploadImage(bill_id: String) {
        val images = imgDao.findByBillID(bill_id,STATUS.NOT_SYNCED)
        if (images.isNotEmpty()) {
            images.forEach { image ->
                var imgFile = File(image.localPath)
                val length = imgFile.length()
                LogUtils.d("图片大小", length)
                if (length > FILE_LENGTH_1M * 3) { //图片超过设定值则压缩
                    LogUtils.d("图片大小超过2M,压缩图片", FILE_LENGTH_1M * 3)
                    val fileList = Luban.with(App.context).load(imgFile).get()
                    if (fileList.isNotEmpty() && fileList.size > 0) {
                        imgFile = fileList[0]
                    }
                }
                val fileName = imgFile.name
                val requestBody = imgFile.asRequestBody("image/png".toMediaTypeOrNull())
                val part: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", fileName, requestBody)
                val time = imgFile.lastModified()
                val objectId = image.id
                val response: BaseResponse<ImageEntity> = network.imageUpload(
                    part,
                    objectId, bill_id, time
                )
                response.data.let {
                    image.onlinePath = response.data._id
                    image.md5 = response.data.md5
                    image.id = response.data._id
                    image.syncStatus = STATUS.SYNCED
                    LogUtils.d("账单图片上传成功：$image")
                    image.onlinePath?.let {
                        var count = App.dataBase.imageDao()
                            .updateOnlinePath(image.id, it, image.syncStatus)
                        if (count > 0)
                            LogUtils.d("图片更新成功：$image")
                    }


                }

            }
        }
    }

    /**
     * 添加账单，保存到数据库就算成功，同步交给AppViewModule
     */
    suspend fun addBill(bill: Bill) {
        network.billPush(bill).let {
            if (it.code == OK) {
                bill.syncStatus = STATUS.SYNCED
                billDao.updateSyncStatus(it.data.toString(),STATUS.SYNCED)
            }
        }
        uploadImage(bill_id = bill.id)

    }
}