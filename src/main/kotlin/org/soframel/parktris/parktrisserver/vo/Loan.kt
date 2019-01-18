package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import java.time.LocalDate

class Loan () {
    @Id
    var id: String?=null
    var owner: String?=null
    var tenant: String?=null
    var slotId: String?=null
    var startDate: LocalDate?=null
    var endDate: LocalDate?=null
    override fun toString(): String {
        return "Loan(id=$id, owner=$owner, tenant=$tenant, slotId=$slotId, startDate=$startDate, endDate=$endDate)"
    }


}