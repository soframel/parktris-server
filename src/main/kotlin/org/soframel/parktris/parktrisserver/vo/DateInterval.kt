package org.soframel.parktris.parktrisserver.vo

import java.time.LocalDate

class DateInterval(var startDate: LocalDate, var endDate: LocalDate) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateInterval

        if (!startDate.isEqual(other.startDate)) return false
        if (!endDate.isEqual(other.endDate)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "DateInterval(startDate=$startDate, endDate=$endDate)"
    }


}