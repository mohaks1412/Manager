package com.finance.manager.service

import com.finance.manager.dto.request.LoginRequest
import com.finance.manager.dto.request.RegisterRequest
import com.finance.manager.dto.response.LoginResponse
import com.finance.manager.dto.response.RegisterResponse
import com.finance.manager.entity.Category
import com.finance.manager.entity.CategoryType
import com.finance.manager.entity.User
import com.finance.manager.exception.DuplicateResourceException
import com.finance.manager.repository.CategoryRepository
import com.finance.manager.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    fun register(request: RegisterRequest): RegisterResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw DuplicateResourceException("Username already exists")
        }

        val user = userRepository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                fullName = request.fullName,
                phoneNumber = request.phoneNumber
            )
        )

        // Create default categories for user
        val defaultCategories = listOf(
            Category(name = "Salary", type = CategoryType.INCOME, isDefault = true, user = null),
            Category(name = "Food", type = CategoryType.EXPENSE, isDefault = true, user = null),
            Category(name = "Rent", type = CategoryType.EXPENSE, isDefault = true, user = null),
            Category(name = "Transportation", type = CategoryType.EXPENSE, isDefault = true, user = null),
            Category(name = "Entertainment", type = CategoryType.EXPENSE, isDefault = true, user = null),
            Category(name = "Healthcare", type = CategoryType.EXPENSE, isDefault = true, user = null),
            Category(name = "Utilities", type = CategoryType.EXPENSE, isDefault = true, user = null)
        )

        // Only save if they don't exist yet
        if (categoryRepository.count() == 0L) {
            categoryRepository.saveAll(defaultCategories)
        }

        return RegisterResponse("User registered successfully", user.id)
    }

    fun login(
        request: LoginRequest,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ): LoginResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        val session = httpRequest.getSession(true)
        val repo = HttpSessionSecurityContextRepository()
        repo.saveContext(SecurityContextHolder.getContext(), httpRequest, httpResponse)

        return LoginResponse("Login successful")
    }

    fun logout(request: HttpServletRequest): String {
        request.getSession(false)?.invalidate()
        SecurityContextHolder.clearContext()
        return "Logout successful"
    }
}