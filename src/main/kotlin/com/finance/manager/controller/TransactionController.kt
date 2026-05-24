package com.finance.manager.controller

import com.finance.manager.dto.request.TransactionRequest
import com.finance.manager.dto.request.UpdateTransactionRequest
import com.finance.manager.dto.response.MessageResponse
import com.finance.manager.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping
    fun createTransaction(
        @Valid @RequestBody request: TransactionRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity(
        transactionService.createTransaction(request, userDetails.username),
        HttpStatus.CREATED
    )

    @GetMapping
    fun getTransactions(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) category: String?
    ) = ResponseEntity.ok(
        transactionService.getTransactions(
            userDetails.username,
            startDate,
            endDate,
            category
        )
    )

    @PutMapping("/{id}")
    fun updateTransaction(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTransactionRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(
        transactionService.updateTransaction(id, request, userDetails.username)
    )

    @DeleteMapping("/{id}")
    fun deleteTransaction(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MessageResponse> {
        transactionService.deleteTransaction(id, userDetails.username)
        return ResponseEntity.ok(MessageResponse("Transaction deleted successfully"))
    }
}