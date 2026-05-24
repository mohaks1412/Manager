package com.finance.manager.dto.response

data class RegisterResponse(
    val message: String,
    val userId: Long
)

data class LoginResponse(
    val message: String
)

data class MessageResponse(
    val message: String
)