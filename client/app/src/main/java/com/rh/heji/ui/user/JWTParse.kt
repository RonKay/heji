package com.rh.heji.ui.user

import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.StringUtils
import com.rh.heji.Config
import org.json.JSONObject


object JWTParse {
    data class User(val name: String, val auth: List<String>, val token: String)

    fun getUser(jwt: String): User {
        if (jwt == "" || jwt ==Config.localUserName) return Config.localUser
        val token = resolveToken(jwt)
        //val index = token.lastIndexOf(".")
        //var withoutSignature = token.substring(0, index)
        val header = token.split(".")[0]
        val payload = token.split(".")[1]
        var untrusted = String(EncodeUtils.base64Decode(payload))
        var jsonObject = JSONObject(untrusted)
        val username = jsonObject.opt("sub") as String
        val auth: String = jsonObject.opt("auth") as String
        val roles = auth.split(",")
        return User(username, roles, jwt)
    }

    private fun resolveToken(token: String): String {
        val bearer = "Bearer "
        return if (!StringUtils.isEmpty(token) && token.startsWith(bearer)) {
            token.substring(bearer.length)
        } else token
    }
}