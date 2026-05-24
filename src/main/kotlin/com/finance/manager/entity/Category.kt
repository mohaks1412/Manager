package com.finance.manager.entity

import jakarta.persistence.*

enum class CategoryType {
    INCOME, EXPENSE
}

@Entity
@Table(name = "categories")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: CategoryType = CategoryType.EXPENSE,

    @Column(nullable = false)
    val isDefault: Boolean = false,

    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    val user: User? = null
)