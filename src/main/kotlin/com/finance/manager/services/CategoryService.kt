package com.finance.manager.service

import com.finance.manager.dto.request.CategoryRequest
import com.finance.manager.dto.response.CategoryListResponse
import com.finance.manager.dto.response.CategoryResponse
import com.finance.manager.entity.Category
import com.finance.manager.exception.BadRequestException
import com.finance.manager.exception.DuplicateResourceException
import com.finance.manager.exception.ForbiddenException
import com.finance.manager.exception.ResourceNotFoundException
import com.finance.manager.repository.CategoryRepository
import com.finance.manager.repository.TransactionRepository
import com.finance.manager.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) {

    fun getAllCategories(username: String): CategoryListResponse {
        val user = userRepository.findByUsername(username)!!
        val categories = categoryRepository.findByUserIdOrUserIsNull(user.id)
        return CategoryListResponse(
            categories.filter { !it.isDeleted }.map {
                CategoryResponse(
                    name = it.name,
                    type = it.type.name,
                    custom = !it.isDefault
                )
            }
        )
    }

    fun createCategory(request: CategoryRequest, username: String): CategoryResponse {
        val user = userRepository.findByUsername(username)!!

        if (categoryRepository.existsByNameAndUserId(request.name, user.id)) {
            throw DuplicateResourceException("Category '${request.name}' already exists")
        }

        val category = categoryRepository.save(
            Category(
                name = request.name,
                type = request.type,
                isDefault = false,
                user = user
            )
        )

        return CategoryResponse(
            name = category.name,
            type = category.type.name,
            custom = true
        )
    }

    fun deleteCategory(name: String, username: String) {
        val user = userRepository.findByUsername(username)!!
        val category = categoryRepository.findByNameAndUser(name, user.id)
            .firstOrNull()
            ?: throw ResourceNotFoundException("Category '$name' not found")

        if (category.isDefault) {
            throw ForbiddenException("Cannot delete default categories")
        }

        if (category.user?.id != user.id) {
            throw ForbiddenException("Cannot delete another user's category")
        }

        if (transactionRepository.existsByCategoryAndIsDeletedFalse(category)) {
            throw BadRequestException("Category is referenced by existing transactions")
        }

        category.isDeleted = true
        categoryRepository.save(category)
    }

    fun getCategoryByNameAndUser(name: String, username: String): Category {
        val user = userRepository.findByUsername(username)!!
        val results = categoryRepository.findByNameAndUser(name, user.id)
        return results.firstOrNull()
            ?: throw ResourceNotFoundException("Category '$name' not found")
    }
}