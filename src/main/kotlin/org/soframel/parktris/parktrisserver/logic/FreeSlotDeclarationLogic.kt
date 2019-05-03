package org.soframel.parktris.parktrisserver.logic

import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.LocalDate


class FreeSlotDeclarationLogic{

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)

    @Autowired
    lateinit var loanRepo: LoanRepository

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