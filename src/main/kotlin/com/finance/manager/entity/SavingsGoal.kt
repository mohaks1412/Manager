package com.finance.manager.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "savings_goals")
data class SavingsGoal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var goalName: String = "",

    @Column(nullable = false)
    var targetAmount: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    var targetDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    val startDate: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User = User(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
)