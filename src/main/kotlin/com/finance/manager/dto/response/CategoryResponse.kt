package com.finance.manager.dto.response

data class CategoryResponse(
    val name: String,
    val type: String,
    val custom: Boolean
)

data class CategoryListResponse(
    val categories: List<CategoryResponse>
)