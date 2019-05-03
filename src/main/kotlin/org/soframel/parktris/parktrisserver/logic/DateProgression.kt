package org.soframel.parktris.parktrisserver.logic

import java.time.LocalDate

class DateProgression(override val start: LocalDate,
                      override val endInclusive: LocalDate,
                      val stepDays: Long = 1) :
        Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> =
            DateDayIterator(start, endInclusive)

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)

}