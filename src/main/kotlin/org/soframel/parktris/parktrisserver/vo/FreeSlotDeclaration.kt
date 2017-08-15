package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import java.time.LocalDate

class FreeSlotDeclaration() {
    @Id
    var id: String=""
    var slotId: String   =""
    var startDate: LocalDate  = LocalDate.MIN
    var endDate: LocalDate     = LocalDate.MIN
    var preferedTenantId: String  =""
}