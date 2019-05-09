package org.soframel.parktris.parktrisserver.vo

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.data.annotation.Id
import java.time.LocalDate

class Loan () {
    @Id
    var id: String?=null
    var owner: String?=null
    var tenant: String?=null
    var declId: String?=null
    var slotId: String?=null

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    var startDate: LocalDate?=null

    @JsonDeserialize(using =LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    var endDate: LocalDate?=null

    override fun toString(): String {
        return "Loan(id=$id, owner=$owner, tenant=$tenant, slotId=$slotId, startDate=$startDate, endDate=$endDate)"
    }


}