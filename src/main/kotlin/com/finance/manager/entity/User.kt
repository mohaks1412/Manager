package com.finance.manager.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val username: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    val fullName: String = "",

    @Column(nullable = false)
    val phoneNumber: String = ""
)