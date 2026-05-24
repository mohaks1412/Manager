package com.finance.manager.service

import com.finance.manager.dto.request.GoalRequest
import com.finance.manager.dto.request.UpdateGoalRequest
import com.finance.manager.dto.response.GoalListResponse
import com.finance.manager.dto.response.GoalResponse
import com.finance.manager.entity.SavingsGoal
import com.finance.manager.exception.BadRequestException
import com.finance.manager.exception.ForbiddenException
import com.finance.manager.exception.ResourceNotFoundException
import com.finance.manager.repository.SavingsGoalRepository
import com.finance.manager.repository.TransactionRepository
import com.finance.manager.repository.UserRepository
import com.finance.manager.entity.CategoryType
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

@Service
class SavingsGoalService(
    private val savingsGoalRepository: SavingsGoalRepository,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) {

    fun createGoal(request: GoalRequest, username: String): GoalResponse {
        val user = userRepository.findByUsername(username)!!
        val targetDate = LocalDate.parse(request.targetDate)
        val startDate = request.startDate?.let { LocalDate.parse(it) } ?: LocalDate.now()

        if (targetDate.isBefore(LocalDate.now()) || targetDate.isEqual(LocalDate.now())) {
            throw BadRequestException("Target date must be in the future")
        }

        val goal = savingsGoalRepository.save(
            SavingsGoal(
                goalName = request.goalName,
                targetAmount = request.targetAmount,
                targetDate = targetDate,
                startDate = startDate,
                user = user
            )
        )

        return mapToResponse(goal, username)
    }

    fun getAllGoals(username: String): GoalListResponse {
        val user = userRepository.findByUsername(username)!!
        val goals = savingsGoalRepository.findByUserIdAndIsDeletedFalse(user.id)
        return GoalListResponse(goals.map { mapToResponse(it, username) })
    }

    fun getGoal(id: Long, username: String): GoalResponse {
        val user = userRepository.findByUsername(username)!!
        val goal = savingsGoalRepository.findByIdAndIsDeletedFalse(id)
            ?: throw ResourceNotFoundException("Goal not found")

        if (goal.user.id != user.id) {
            throw ForbiddenException("Access denied")
        }

        return mapToResponse(goal, username)
    }

    fun updateGoal(id: Long, request: UpdateGoalRequest, username: String): GoalResponse {
        val user = userRepository.findByUsername(username)!!
        val goal = savingsGoalRepository.findByIdAndIsDeletedFalse(id)
            ?: throw ResourceNotFoundException("Goal not found")

        if (goal.user.id != user.id) {
            throw ForbiddenException("Access denied")
        }

        request.targetAmount?.let { goal.targetAmount = it }
        request.targetDate?.let {
            val targetDate = LocalDate.parse(it)
            if (targetDate.isBefore(LocalDate.now()) || targetDate.isEqual(LocalDate.now())) {
                throw BadRequestException("Target date must be in the future")
            }
            goal.targetDate = targetDate
        }

        return mapToResponse(savingsGoalRepository.save(goal), username)
    }

    fun deleteGoal(id: Long, username: String) {
        val user = userRepository.findByUsername(username)!!
        val goal = savingsGoalRepository.findByIdAndIsDeletedFalse(id)
            ?: throw ResourceNotFoundException("Goal not found")

        if (goal.user.id != user.id) {
            throw ForbiddenException("Access denied")
        }

        goal.isDeleted = true
        savingsGoalRepository.save(goal)
    }

    private fun calculateProgress(goal: SavingsGoal, username: String): BigDecimal {
        val user = userRepository.findByUsername(username)!!
        val transactions = transactionRepository
            .findByUserIdAndIsDeletedFalseAndDateBetweenOrderByDateDesc(
                user.id,
                goal.startDate,
                LocalDate.now()
            )

        val income = transactions
            .filter { it.category.type == CategoryType.INCOME }
            .sumOf { it.amount }

        val expenses = transactions
            .filter { it.category.type == CategoryType.EXPENSE }
            .sumOf { it.amount }

        return income - expenses
    }

    private fun mapToResponse(goal: SavingsGoal, username: String): GoalResponse {
        val progress = calculateProgress(goal, username)
        val percentage = if (goal.targetAmount > BigDecimal.ZERO) {
            val raw = progress.divide(goal.targetAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)
            if (raw.stripTrailingZeros().scale() <= 0)
                raw.setScale(1, RoundingMode.HALF_UP)
            else
                raw.stripTrailingZeros()
        } else BigDecimal("0.0")

        val remaining = (goal.targetAmount - progress)
            .coerceAtLeast(BigDecimal.ZERO)

        return GoalResponse(
            id = goal.id,
            goalName = goal.goalName,
            targetAmount = goal.targetAmount,
            targetDate = goal.targetDate.toString(),
            startDate = goal.startDate.toString(),
            currentProgress = progress,
            progressPercentage = percentage,
            remainingAmount = remaining
        )
    }
}