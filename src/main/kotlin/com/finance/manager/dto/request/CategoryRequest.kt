package com.finance.manager.dto.request

import com.finance.manager.entity.CategoryType
import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank val name: String,
    val type: CategoryType
)