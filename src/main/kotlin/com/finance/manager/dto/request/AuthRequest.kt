package com.finance.manager.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:Email val username: String,
    @field:NotBlank val password: String,
    @field:NotBlank val fullName: String,
    @field:NotBlank val phoneNumber: String
)

data class LoginRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String
)