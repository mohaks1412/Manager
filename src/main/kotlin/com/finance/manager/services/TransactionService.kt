package com.finance.manager.service

import com.finance.manager.dto.request.TransactionRequest
import com.finance.manager.dto.request.UpdateTransactionRequest
import com.finance.manager.dto.response.TransactionListResponse
import com.finance.manager.dto.response.TransactionResponse
import com.finance.manager.entity.Transaction
import com.finance.manager.exception.BadRequestException
import com.finance.manager.exception.ForbiddenException
import com.finance.manager.exception.ResourceNotFoundException
import com.finance.manager.repository.TransactionRepository
import com.finance.manager.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val categoryService: CategoryService
) {

    fun createTransaction(request: TransactionRequest, username: String): TransactionResponse {
        val user = userRepository.findByUsername(username)!!
        val date = try {
            LocalDate.parse(request.date)
        } catch (e: Exception) {
            throw BadRequestException("Invalid date format. Use YYYY-MM-DD")
        }

        if (date.isAfter(LocalDate.now())) {
            throw BadRequestException("Transaction date cannot be in the future")
        }

        val category = categoryService.getCategoryByNameAndUser(request.category, username)

        val transaction = transactionRepository.save(
            Transaction(
                amount = request.amount,
                date = date,
                category = category,
                description = request.description,
                user = user
            )
        )

        return mapToResponse(transaction)
    }

    fun getTransactions(
        username: String,
        startDate: String?,
        endDate: String?,
        category: String?
    ): TransactionListResponse {
        val user = userRepository.findByUsername(username)!!

        val transactions = when {
            startDate != null && endDate != null -> {
                transactionRepository
                    .findByUserIdAndIsDeletedFalseAndDateBetweenOrderByDateDesc(
                        user.id,
                        LocalDate.parse(startDate),
                        LocalDate.parse(endDate)
                    ).filter { category == null || it.category.name == category }
            }
            category != null -> {
                transactionRepository
                    .findByUserIdAndIsDeletedFalseOrderByDateDesc(user.id)
                    .filter { it.category.name == category }
            }
            else -> transactionRepository
                .findByUserIdAndIsDeletedFalseOrderByDateDesc(user.id)
        }

        return TransactionListResponse(transactions.map { mapToResponse(it) })
    }

    fun updateTransaction(
        id: Long,
        request: UpdateTransactionRequest,
        username: String
    ): TransactionResponse {
        val user = userRepository.findByUsername(username)!!
        val transaction = transactionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Transaction not found") }

        if (transaction.user.id != user.id) {
            throw ForbiddenException("Access denied")
        }

        if (transaction.isDeleted) {
            throw ResourceNotFoundException("Transaction not found")
        }

        request.amount?.let { transaction.amount = it }
        request.description?.let { transaction.description = it }
        request.category?.let {
            transaction.category = categoryService.getCategoryByNameAndUser(it, username)
        }

        return mapToResponse(transactionRepository.save(transaction))
    }

    fun deleteTransaction(id: Long, username: String) {
        val user = userRepository.findByUsername(username)!!
        val transaction = transactionRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Transaction not found") }

        if (transaction.user.id != user.id) {
            throw ForbiddenException("Access denied")
        }

        if (transaction.isDeleted) {
            throw ResourceNotFoundException("Transaction not found")
        }

        transaction.isDeleted = true
        transactionRepository.save(transaction)
    }

    fun mapToResponse(transaction: Transaction) = TransactionResponse(
        id = transaction.id,
        amount = transaction.amount,
        date = transaction.date.toString(),
        category = transaction.category.name,
        description = transaction.description,
        type = transaction.category.type.name
    )
}