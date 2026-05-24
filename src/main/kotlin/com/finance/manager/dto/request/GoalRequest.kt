package com.finance.manager.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class GoalRequest(
    @field:NotBlank val goalName: String,
    @field:Positive val targetAmount: BigDecimal,
    @field:NotBlank val targetDate: String,
    val startDate: String? = null
)

data class UpdateGoalRequest(
    @field:Positive val targetAmount: BigDecimal? = null,
    val targetDate: String? = null
)