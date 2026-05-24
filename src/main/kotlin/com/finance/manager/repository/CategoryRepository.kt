package com.finance.manager.repository

import com.finance.manager.entity.Category
import com.finance.manager.entity.CategoryType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByUserIdOrUserIsNull(userId: Long): List<Category>

    @Query("SELECT c FROM Category c WHERE c.name = :name AND c.isDeleted = false AND (c.user.id = :userId OR c.user IS NULL)")
    fun findByNameAndUser(name: String, userId: Long): List<Category>

    fun existsByNameAndUserId(name: String, userId: Long): Boolean
}