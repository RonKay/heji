package com.rh.heji.ui.user.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.rh.heji.ui.user.login.LoginActivity
import com.rh.heji.R
import com.rh.heji.databinding.FragmentRegisterBinding
import com.rh.heji.render


class RegisterFragment : Fragment() {
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }
    private val binding: FragmentRegisterBinding by lazy {
        FragmentRegisterBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        renderView()
        return binding.root
    }

    private fun initView() {
        binding.btnRegister.setOnClickListener {
            val password1 = binding.editPassword.text.toString()
            val password2 = binding.editPassword2.text.toString()
            if (password1 != password2) {
                ToastUtils.showLong("两次输入的密码不一致")
                return@setOnClickListener
            }
            val code = binding.editInviteCode.text.toString()
            val tel = binding.editTEL.text.toString()
            val username = binding.editUserName.text.toString()
            viewModel.doAction(RegisterAction.Register(username, tel, code, password1))
        }
    }

    private fun renderView() {
        render(viewModel) {
            when (it) {
                is RegisterUiState.Success -> {
                    toLogin(it.user)
                }
                is RegisterUiState.Error -> {
                    ToastUtils.showLong(it.throwable.message)
                }
            }
        }
    }


    private fun toLogin(user: RegisterUser) {
        var mBundle = Bundle()
        mBundle.putSerializable("user", user)
        Navigation.findNavController(binding.root).popBackStack()
        Navigation.findNavController(binding.root).navigate(R.id.nav_login, mBundle)
    }


    override fun onResume() {
        super.onResume()
        setTitle()
    }

    private fun setTitle() {
        with(activity as LoginActivity) {
            findViewById<Toolbar>(R.id.toolbar).title = getString(R.string.register)
            this
        }
    }
}