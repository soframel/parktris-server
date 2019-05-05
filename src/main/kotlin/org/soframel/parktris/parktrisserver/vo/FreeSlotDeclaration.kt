package org.soframel.parktris.parktrisserver.vo

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.data.annotation.Id
import java.time.LocalDate
import kotlin.reflect.KClass

open class FreeSlotDeclaration() {
    @Id
    var id: String?=null
    var owner: String?=null
    var slotId: String?=null


    @JsonDeserialize(using =LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    var startDate: LocalDate?=null
    var endDate: LocalDate?=null
    var preferedTenant: MutableList<String>?=null
    override fun toString(): String {
        return "FreeSlotDeclaration(id=$id, owner=$owner, slotId=$slotId, startDate=$startDate, endDate=$endDate, preferedTenant=$preferedTenant)"
    }


}