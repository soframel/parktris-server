package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import java.time.LocalDate

class Loan () {
    @Id
    lateinit var id: String
    lateinit var ownerId: String
    lateinit var tenantId: String
    lateinit var slotId: String
    lateinit var startDate: LocalDate
    lateinit var endDate: LocalDate
}