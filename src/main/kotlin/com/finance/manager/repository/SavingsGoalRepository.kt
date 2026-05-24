package com.finance.manager.repository

import com.finance.manager.entity.SavingsGoal
import org.springframework.data.jpa.repository.JpaRepository

interface SavingsGoalRepository : JpaRepository<SavingsGoal, Long> {
    fun findByUserIdAndIsDeletedFalse(userId: Long): List<SavingsGoal>
    fun findByIdAndIsDeletedFalse(id: Long): SavingsGoal?
}