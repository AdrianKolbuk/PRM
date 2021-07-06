package pl.pjatk.managerfinansowy.model

import java.time.LocalDate

data class Fund(
        val place: String,
        val amount: Double,
        val date: LocalDate,
        val category: String,
        val option: String
)