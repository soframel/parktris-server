package org.soframel.parktris.parktrisserver.controllers

import org.slf4j.LoggerFactory
import org.soframel.parktris.parktrisserver.logic.LoanLogic
import org.soframel.parktris.parktrisserver.repositories.FreeSlotDeclarationRepository
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController
class LoanController {

    var logger = LoggerFactory.getLogger(LoanController::class.java)

    @Autowired
    lateinit var loanRepo: LoanRepository

    @Autowired
    lateinit var freeSlotDeclRepo: FreeSlotDeclarationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var loanLogic: LoanLogic


    @PostMapping(value = ["/loans"], produces = ["application/json"])
    fun createLoan(@RequestBody loan: Loan, principal: Principal): ResponseEntity<Loan> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            logger.debug("storing loan "+loan)
            var decl=freeSlotDeclRepo.findById(loan.declId!!)
            if(decl!=null && decl.isPresent && loanLogic.isValidLoan(loan, decl.get(), principal.name)){
                loan.tenant = user.login
                var result = loanRepo.save(loan)
                logger.debug("saved loan: "+result)
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


    @PreAuthorize("#tenant == principal.username")
    @GetMapping(value = ["/loans"], produces= ["application/json"])
    fun getOwnLoans(@RequestParam("tenant") tenant: String, principal: Principal): ResponseEntity<List<Loan>> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null && user.login==tenant) {
            logger.debug("listing loans for user"+principal.name)
            var result = loanRepo.findAllByTenant(principal.name)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            logger.error("no user found, or wrong user")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }


    @PreAuthorize("#tenant == principal.username")
    @GetMapping(value = ["/loans/future"], produces= ["application/json"])
    fun getOwnFutureLoans(@RequestParam("tenant") tenant: String, principal: Principal): ResponseEntity<List<Loan>> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null && user.login==tenant) {
            logger.debug("listing future loans for user"+principal.name)
            var result = loanRepo.findFutureLoansByTenant(Date(), principal.name)
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            logger.error("no user found, or wrong user")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }


    @DeleteMapping(value = ["/loans/{id}"])
    fun deleteLoan(@PathVariable("id") id: String, principal: Principal): ResponseEntity<Void> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            var loan=loanRepo.findById(id)
            if(loan==null || !loan.isPresent){
                logger.error("loan $id not found")
                return return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
            else{
                var loan2=loan.get()
                if(loan2.tenant!=user.login){
                    logger.error("unauthorized for user "+principal.name)
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    logger.debug("deleting loan $id")
                    loanRepo.delete(loan2)
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                }
            }
        } else {
            logger.error("no user found")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }
}