package com.finance.manager.controller

import com.finance.manager.dto.request.LoginRequest
import com.finance.manager.dto.request.RegisterRequest
import com.finance.manager.dto.response.MessageResponse
import com.finance.manager.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ) = ResponseEntity(authService.register(request), HttpStatus.CREATED)

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ) = ResponseEntity.ok(authService.login(request, httpRequest, httpResponse))

    @PostMapping("/logout")
    fun logout(
        httpRequest: HttpServletRequest
    ) = ResponseEntity.ok(MessageResponse(authService.logout(httpRequest)))
}