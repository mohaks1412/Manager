package com.finance.manager.dto.response

import java.math.BigDecimal

data class GoalResponse(
    val id: Long,
    val goalName: String,
    val targetAmount: BigDecimal,
    val targetDate: String,
    val startDate: String,
    val currentProgress: BigDecimal,
    val progressPercentage: BigDecimal,
    val remainingAmount: BigDecimal
)

data class GoalListResponse(
    val goals: List<GoalResponse>
)