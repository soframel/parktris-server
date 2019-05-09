package org.soframel.parktris.parktrisserver.logic

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class LoanLogic {

    var logger= Logger.getLogger(org.soframel.parktris.parktrisserver.logic.FreeSlotDeclarationLogic::class.java)

    operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)


    @Autowired
    lateinit var declLogic: FreeSlotDeclarationLogic

    @Autowired
    lateinit var loanRepo: LoanRepository


    fun isValidLoan(loan: Loan, decl: FreeSlotDeclaration, user: String): Boolean{
        var start=loan.startDate
        if(start==null){
            logger.error("no start date to loan")
            return false
        }
        var end=loan.endDate
        if(end==null){
            logger.error("no end date to loan")
            return false
        }

        //slot id
        if(loan.slotId==null){
            logger.error("no slotId for loan")
            return false;
        }

        //end after start
        var datesOrdered= (start!=null && end!=null && (start.isEqual(end) || start.isBefore(end)))
        if(!datesOrdered){
            logger.error("dates of loan are not ordered: startDate=$start, endDate=$end")
            return false;
        }

        //check that slot declaration exists
        if(decl==null){
            logger.error("no matching free slot declaration found with id=${loan.declId}")
            return false;
        }

        //check that dates match
        var startDecl=decl.startDate!!
        if(startDecl.isAfter(start)){
            logger.error("start date $start  requested after declaration start date ${decl.startDate}")
            return false
        }
        var endDecl=decl.endDate!!
        if(end.isAfter(endDecl)){
            logger.error("end date $end  requested after declaration end date ${decl.endDate}")
            return false
        }

        //check that no preferred tenant, or this user is included
        var tenants=decl.preferedTenant
        if(tenants!=null && tenants.isNotEmpty()){
            return tenants.contains(user)
        }

        //check that no other loan matches the same dates & decl
        return this.checkLoanDates(decl, loan)
    }


    fun checkLoanDates(decl: FreeSlotDeclaration, loan: Loan): Boolean{
        var loans = loanRepo.findAllByDeclId(decl.id!!)
        var declRange = decl.startDate!!.rangeTo(decl.endDate!!)
        var remainingDates = declLogic.removeNonAvailableDates(declRange, loans)

        //check that all loans dates are in remainingDates
        var loanRange=loan.startDate!!.rangeTo(loan.endDate!!)
        var notAvailableDate= loanRange.find { date -> !remainingDates.contains(date) }
        return (notAvailableDate==null)
    }
}