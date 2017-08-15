package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import java.time.LocalDate

class Loan () {
    @Id
    var id: String=""
    var ownerId: String  =""
    var tenantId: String  =""
    var slotId: String =""
    var startDate: LocalDate = LocalDate.MIN
    var endDate: LocalDate= LocalDate.MIN
}