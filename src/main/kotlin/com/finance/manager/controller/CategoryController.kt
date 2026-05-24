package com.finance.manager.controller

import com.finance.manager.dto.request.CategoryRequest
import com.finance.manager.dto.response.MessageResponse
import com.finance.manager.service.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getAllCategories(
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(categoryService.getAllCategories(userDetails.username))

    @PostMapping
    fun createCategory(
        @Valid @RequestBody request: CategoryRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity(
        categoryService.createCategory(request, userDetails.username),
        HttpStatus.CREATED
    )

    @DeleteMapping("/{name}")
    fun deleteCategory(
        @PathVariable name: String,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MessageResponse> {
        categoryService.deleteCategory(name, userDetails.username)
        return ResponseEntity.ok(MessageResponse("Category deleted successfully"))
    }
}