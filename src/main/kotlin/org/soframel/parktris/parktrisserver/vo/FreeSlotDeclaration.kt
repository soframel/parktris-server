package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import java.time.LocalDate

class FreeSlotDeclaration() {
    @Id
    var id: String?=null
    var slotId: String?=null
    var startDate: LocalDate?=null
    var endDate: LocalDate?=null
    var preferedTenantId: String?=null
}