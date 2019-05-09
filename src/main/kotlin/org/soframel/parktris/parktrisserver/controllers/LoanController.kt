package org.soframel.parktris.parktrisserver.controllers

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.logic.LoanLogic
import org.soframel.parktris.parktrisserver.repositories.FreeSlotDeclarationRepository
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class LoanController {

    var logger = Logger.getLogger(LoanController::class.java)

    @Autowired
    lateinit var loanRepo: LoanRepository

    @Autowired
    lateinit var freeSlotDeclRepo: FreeSlotDeclarationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var loanLogic: LoanLogic


    @PostMapping(value = "/loans", produces = ["application/json"])
    fun createLoan(@RequestBody loan: Loan, principal: Principal): ResponseEntity<Loan> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            logger.debug("storing loan "+loan)
            var decl=freeSlotDeclRepo.findOne(loan.declId)
            if(loanLogic.isValidLoan(loan, decl, principal.name)){
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