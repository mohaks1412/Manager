package com.finance.manager.repository

import com.finance.manager.entity.Transaction
import com.finance.manager.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByUserIdAndIsDeletedFalseOrderByDateDesc(userId: Long): List<Transaction>
    fun findByUserIdAndIsDeletedFalseAndDateBetweenOrderByDateDesc(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Transaction>
    fun findByUserIdAndIsDeletedFalseAndCategoryOrderByDateDesc(
        userId: Long,
        category: Category
    ): List<Transaction>
    fun existsByCategoryAndIsDeletedFalse(category: Category): Boolean

    fun findByUserIdAndIsDeletedFalseAndCategoryIdOrderByDateDesc(
        userId: Long,
        categoryId: Long
    ): List<Transaction>
}
