package com.finance.manager.controller

import com.finance.manager.service.ReportService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportService: ReportService
) {

    @GetMapping("/monthly/{year}/{month}")
    fun getMonthlyReport(
        @PathVariable year: Int,
        @PathVariable month: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(
        reportService.getMonthlyReport(userDetails.username, year, month)
    )

    @GetMapping("/yearly/{year}")
    fun getYearlyReport(
        @PathVariable year: Int,
        @AuthenticationPrincipal userDetails: UserDetails
    ) = ResponseEntity.ok(
        reportService.getYearlyReport(userDetails.username, year)
    )
}