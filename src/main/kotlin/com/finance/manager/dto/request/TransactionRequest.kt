package com.finance.manager.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class TransactionRequest(
    @field:Positive val amount: BigDecimal,
    @field:NotBlank val date: String,
    @field:NotBlank val category: String,
    val description: String? = null
)

data class UpdateTransactionRequest(
    @field:Positive val amount: BigDecimal? = null,
    val description: String? = null,
    val category: String? = null
)