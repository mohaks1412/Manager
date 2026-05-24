package com.finance.manager.service

import com.finance.manager.dto.response.MonthlyReportResponse
import com.finance.manager.dto.response.YearlyReportResponse
import com.finance.manager.entity.CategoryType
import com.finance.manager.repository.TransactionRepository
import com.finance.manager.repository.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import com.finance.manager.exception.BadRequestException

@Service
class ReportService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {

    fun getMonthlyReport(username: String, year: Int, month: Int): MonthlyReportResponse {
        if (month < 1 || month > 12) {
            throw BadRequestException("Invalid month: $month. Must be between 1 and 12")
        }
        val user = userRepository.findByUsername(username)!!
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth())

        val transactions = transactionRepository
            .findByUserIdAndIsDeletedFalseAndDateBetweenOrderByDateDesc(
                user.id, startDate, endDate
            )

        val totalIncome = mutableMapOf<String, BigDecimal>()
        val totalExpenses = mutableMapOf<String, BigDecimal>()

        transactions.forEach { transaction ->
            val categoryName = transaction.category.name
            val amount = transaction.amount

            if (transaction.category.type == CategoryType.INCOME) {
                totalIncome[categoryName] =
                    (totalIncome[categoryName] ?: BigDecimal.ZERO) + amount
            } else {
                totalExpenses[categoryName] =
                    (totalExpenses[categoryName] ?: BigDecimal.ZERO) + amount
            }
        }

        val netSavings = totalIncome.values.sumOf { it } -
                totalExpenses.values.sumOf { it }

        return MonthlyReportResponse(
            month = month,
            year = year,
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            netSavings = netSavings
        )
    }

    fun getYearlyReport(username: String, year: Int): YearlyReportResponse {
        val user = userRepository.findByUsername(username)!!
        val startDate = LocalDate.of(year, 1, 1)
        val endDate = LocalDate.of(year, 12, 31)

        val transactions = transactionRepository
            .findByUserIdAndIsDeletedFalseAndDateBetweenOrderByDateDesc(
                user.id, startDate, endDate
            )

        val totalIncome = mutableMapOf<String, BigDecimal>()
        val totalExpenses = mutableMapOf<String, BigDecimal>()

        transactions.forEach { transaction ->
            val categoryName = transaction.category.name
            val amount = transaction.amount

            if (transaction.category.type == CategoryType.INCOME) {
                totalIncome[categoryName] =
                    (totalIncome[categoryName] ?: BigDecimal.ZERO) + amount
            } else {
                totalExpenses[categoryName] =
                    (totalExpenses[categoryName] ?: BigDecimal.ZERO) + amount
            }
        }

        val netSavings = totalIncome.values.sumOf { it } -
                totalExpenses.values.sumOf { it }

        return YearlyReportResponse(
            year = year,
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            netSavings = netSavings
        )
    }
}