package org.soframel.parktris.parktrisserver.logic

import java.time.LocalDate

class DateDayIterator(val startDate: LocalDate,
                   val endDateInclusive: LocalDate): Iterator<LocalDate> {
    private var currentDate = startDate

    override fun hasNext() = currentDate <= endDateInclusive

    override fun next(): LocalDate {

        val next = currentDate

        currentDate = currentDate.plusDays(1)

        return next

    }

}