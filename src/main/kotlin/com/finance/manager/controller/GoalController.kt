package com.finance.manager.controller

import com.finance.manager.dto.request.GoalRequest
import com.finance.manager.dto.request.UpdateGoalRequest
import com.finance.manager.dto.response.MessageResponse
import com.finance.manager.service.SavingsGoalService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/goals")
class GoalController(
    private val savingsGoalService: SavingsGoalService
) {

    @PostMapping
    fun createGoal(
        @Valid @RequestBody request: GoalRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity(
        savingsGoalService.createGoal(request, userDetails.username),
        HttpStatus.CREATED
    )

    @GetMapping
    fun getAllGoals(
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(savingsGoalService.getAllGoals(userDetails.username))

    @GetMapping("/{id}")
    fun getGoal(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(savingsGoalService.getGoal(id, userDetails.username))

    @PutMapping("/{id}")
    fun updateGoal(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateGoalRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(
        savingsGoalService.updateGoal(id, request, userDetails.username)
    )

    @DeleteMapping("/{id}")
    fun deleteGoal(
        @PathVariable id: Long,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<MessageResponse> {
        savingsGoalService.deleteGoal(id, userDetails.username)
        return ResponseEntity.ok(MessageResponse("Goal deleted successfully"))
    }
}