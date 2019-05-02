package org.soframel.parktris.parktrisserver.services

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.FreeSlotDeclarationRepository
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class LoanService {

    var logger = Logger.getLogger(LoanService::class.java)

    @Autowired
    lateinit var loanRepo: LoanRepository

    @Autowired
    lateinit var freeSlotDeclRepo: FreeSlotDeclarationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    fun isValidLoan(loan: Loan, user: String): Boolean{
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

        //end after start
        var datesOrdered= (start!=null && end!=null && (start.isEqual(end) || start.isBefore(end)))
                && loan.slotId!=null;
        if(!datesOrdered){
            logger.error("dates of loan are not ordered: startDate=$start, endDate=$end")
            return false;
        }

        //check that slot declaration exists
        var decl=freeSlotDeclRepo.findOne(loan.declId)
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

        return true
    }

    @PostMapping(value = "/loans", produces = ["application/json"])
    fun createLoan(@RequestBody loan: Loan, principal: Principal): ResponseEntity<Loan> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            logger.debug("storing loan "+loan)
            if(isValidLoan(loan, principal.name)){
                loan.tenant = user.login
                var result = loanRepo.save(loan)
                logger.debug("creating loan")
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else{
                logger.error("problem with validity, loan=$loan")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
        } else {
            logger.error("no user found for storing loan")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }


}