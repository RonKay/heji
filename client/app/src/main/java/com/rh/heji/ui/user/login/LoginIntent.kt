package com.rh.heji.ui.user.login

import com.rh.heji.ui.base.IAction
import com.rh.heji.ui.base.IUiState

/**
 *
 * @date 2022/9/30
 * @author 锅得铁
 * @since v1.0
 */
internal sealed interface LoginAction : IAction {
    class Login(val userName: String, val password: String) : LoginAction
    object EnableOfflineMode : LoginAction
}

internal sealed interface LoginUiState : IUiState {
    class LoginSuccess(val token: String) : LoginUiState
    class LoginError(val t: Throwable) : LoginUiState
    object OfflineRun : LoginUiState
}