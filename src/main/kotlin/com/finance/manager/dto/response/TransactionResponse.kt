package com.finance.manager.dto.response

import java.math.BigDecimal

data class TransactionResponse(
    val id: Long,
    val amount: BigDecimal,
    val date: String,
    val category: String,
    val description: String?,
    val type: String
)

data class TransactionListResponse(
    val transactions: List<TransactionResponse>
)