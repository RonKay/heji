package com.rh.heji.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.UriUtils
import com.lxj.xpopup.XPopup
import com.rh.heji.App
import com.rh.heji.R
import com.rh.heji.databinding.FragmentSettingBinding
import com.rh.heji.render
import com.rh.heji.ui.base.BaseFragment
import java.io.FileDescriptor

class SettingFragment : BaseFragment() {
    private val REQ_CODE_ALIPAY = 90001
    private val REQ_CODE_WEIXINPAY = 90002
    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel by lazy { ViewModelProvider(this)[SettingViewModel::class.java] }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun initView(rootView: View) {
        binding.inputETC.setOnClickListener { findNavController().navigate(R.id.nav_input_etc) }
        binding.exportQianJi.setOnClickListener { findNavController().navigate(R.id.nav_export) }
        binding.inputAliPay.setOnClickListener {
            selectInputFile(REQ_CODE_ALIPAY)
        }
        binding.inputWeiXinPay.setOnClickListener {
            selectInputFile(REQ_CODE_WEIXINPAY)
        }
        render()
    }

    private fun selectInputFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            setType("*/*")
            addCategory(Intent.CATEGORY_OPENABLE)
            //putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, requestCode)
    }

    private val loadingDialog by lazy {
        XPopup.Builder(this.context).asLoading()
    }

    private fun render() {
        render(viewModel) {
            when (it) {
                is SettingUiState.InputEnd -> {
                    loadingDialog.setTitle(it.title)
                    loadingDialog.show()
                    rootView.postDelayed({ loadingDialog.dismiss() }, 1000)
                }
                is SettingUiState.InputError -> {
                    loadingDialog.setTitle(it.title)
                    loadingDialog.show()
                    rootView.postDelayed({ loadingDialog.dismiss() }, 1000)
                }
                is SettingUiState.InputReading -> {
                    loadingDialog.setTitle(it.title)
                    loadingDialog.show()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_ALIPAY && resultCode == Activity.RESULT_OK) {
            val fileName = getInputFile(data)
            viewModel.doAction(SettingAction.InputAliPayData(fileName))
        }
        if (requestCode == REQ_CODE_WEIXINPAY && resultCode == Activity.RESULT_OK) {
            val fileName = getInputFile(data)
            viewModel.doAction(SettingAction.InputWeiXInData(fileName))
        }
    }

    private fun getInputFile(data: Intent?): String {
        var fileName = ""
        data?.data.also { uri ->
            // Perform operations on the document using its URI.
            val parcelFileDescriptor: ParcelFileDescriptor =
                App.context.contentResolver.openFileDescriptor(uri!!, "r")!!
            val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
            val csv = UriUtils.uri2File(uri).absolutePath
            LogUtils.d("cache file", csv)
            fileName = csv
        }
        LogUtils.d(fileName)
        return fileName
    }

    override fun layout() = binding.root

    override fun setUpToolBar() {
        super.setUpToolBar()
        toolBar.title = "设置"
        toolBar.navigationIcon = blackDrawable()
        toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}