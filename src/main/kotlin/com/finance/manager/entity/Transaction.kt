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
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var amount: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val date: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category = Category(),

    @Column
    var description: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User = User(),

    @Column(nullable = false)
    var isDeleted: Boolean = false
)