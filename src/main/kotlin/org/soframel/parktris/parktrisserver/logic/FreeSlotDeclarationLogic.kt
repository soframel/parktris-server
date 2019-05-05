package org.soframel.parktris.parktrisserver.logic

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.vo.DateInterval
import org.soframel.parktris.parktrisserver.vo.DeclarationWithAvailabilities
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import java.time.DayOfWeek
import java.time.LocalDate


class FreeSlotDeclarationLogic{

    var logger= Logger.getLogger(FreeSlotDeclarationLogic::class.java)

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    @Autowired
    lateinit var loanRepo: LoanRepository

    /**
     * add availabilities for each declaration + remove declarations with no availability
     */
    fun addDeclarationAvailabilities(decls: List<FreeSlotDeclaration>): List<DeclarationWithAvailabilities>{
        return decls.map { decl: FreeSlotDeclaration -> this.extendFreeSlotDeclaration(decl) }.filter { !it.availabilities.isEmpty() }
    }

    fun extendFreeSlotDeclaration(decl: FreeSlotDeclaration): DeclarationWithAvailabilities{
        var loans = loanRepo.findAllByDeclId(decl.id!!)
        var range = decl.startDate!!.rangeTo(decl.endDate!!)
        var remainingDates = this.removeNonAvailableDates(range, loans)
        var result= DeclarationWithAvailabilities(decl)
        result.availabilities=this.getDateInvervalsFromDates(remainingDates)
        return result
    }

    fun getDateInvervalsFromDates(dates: List<LocalDate>): List<DateInterval>{
        var result= mutableListOf<DateInterval>()
        var currentInterval: DateInterval?=null
        var i=0
        var size=dates.size
        while(i<size){
            var date=dates[i]
            if(currentInterval==null){
                currentInterval=DateInterval(date, date)
                i++
            }
            else if(i<size-1) {  //check end date
                    var j = i + 1
                    var sameInterval=true
                    while(j<size && sameInterval){
                        var nextDate=dates[j]
                        if(this.isNextBusinessDay(date, nextDate)){
                            currentInterval!!.endDate=nextDate
                            date=nextDate
                        }
                        else{
                            sameInterval=false
                            i=j
                            result.add(currentInterval!!)
                            currentInterval=null
                        }
                        j++
                    }
                    if(j==size-1 && sameInterval){
                        //we finished the list
                        i=j
                        result.add(currentInterval!!)
                    }
            }
            else{
                //last day of list
                currentInterval.endDate=date
                result.add(currentInterval)
                i++
            }
        }
        return result
    }

    /**
     * checks if date2 is next business day to date1.
     * this function is inclusive of weekends: it responds true for a monday after a friday, but also for a saturday after a friday.
     */
    fun isNextBusinessDay(date1: LocalDate, date2: LocalDate): Boolean{
        if(date1.plusDays(1).isEqual(date2)){
            //simple case, no weekend
            return true
        }
        else{
            var nextDay=date1.plusDays(1)
            var nextNextDay=nextDay.plusDays(1)
            var thirdDay=nextNextDay.plusDays(1)
            return (nextDay.dayOfWeek.equals(DayOfWeek.SATURDAY) && (date2.isEqual(thirdDay) || date2.isEqual(nextNextDay)))
                    || (nextDay.dayOfWeek.equals(DayOfWeek.SUNDAY) && date2.isEqual(nextNextDay))
        }
    }

    fun removeNotAvailableDeclarations(decls: List<FreeSlotDeclaration>): List<FreeSlotDeclaration>{
        return decls.filter {
            var loans = loanRepo.findAllByDeclId(it.id!!)
            this.isDeclarationAvailable(it, loans)
        }
    }

    fun isDeclarationAvailable(decl: FreeSlotDeclaration, loans: List<Loan>): Boolean{
        var range = decl.startDate!!.rangeTo(decl.endDate!!)
        var remainingDates = this.removeNonAvailableDates(range, loans)
        return !(remainingDates.isEmpty())
    }

    fun removeNonAvailableDates(range: DateProgression, loans: List<Loan>): List<LocalDate>{
        var dates=range.toMutableList()
        for(loan in loans){
            dates.removeAll(loan.startDate!!.rangeTo(loan.endDate!!))
        }
        return dates
    }

}