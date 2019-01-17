package org.soframel.parktris.parktrisserver.services

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.FreeSlotDeclarationRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class FreeSlotDeclarationService {

    var logger = Logger.getLogger(FreeSlotDeclarationService::class.java)

    @Autowired
    lateinit var freeSlotDeclRepo: FreeSlotDeclarationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    fun isDeclValid(decl: FreeSlotDeclaration): Boolean{
        var start=decl.startDate
        var end=decl.endDate
        return (start!=null && end!=null && (start.isEqual(end) || start.isBefore(end)))
        && decl.slotId!=null;
    }

    @PostMapping(value = "/declarations", produces = ["application/json"])
    fun createFreeSlotDeclaration(@RequestBody decl: FreeSlotDeclaration, principal: Principal): ResponseEntity<FreeSlotDeclaration> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            logger.debug("storing declaration "+decl)
            if(isDeclValid(decl)){
                decl.owner = user.login
                var result = freeSlotDeclRepo.save(decl)
                logger.debug("creating declaration")
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else{
                logger.error("problem with validity, decl=$decl")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
        } else {
            logger.error("no user found for storing declaration")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PreAuthorize("#owner == principal.username")
    @GetMapping(value = "/declarations", produces= ["application/json"])
    fun getDeclarationsFromOwner(@RequestParam("owner") owner: String, principal: Principal): ResponseEntity<List<FreeSlotDeclaration>> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null && user.login==owner) {
                logger.debug("listing declarations for user"+principal.name)
                var result = freeSlotDeclRepo.findAllByOwner(principal.name)
                return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            logger.error("no user found, or wrong user")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping(value = "/declarations/{id}")
    fun deleteFreeSlotDeclaration(@PathVariable("id") id: String, principal: Principal): ResponseEntity<Void> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            var decl=freeSlotDeclRepo.findOne(id)
            if(decl==null){
                logger.error("declaration $id not found")
                return return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
            else{
                if(decl.owner!=user.login){
                    logger.error("unauthorized for user "+principal.name)
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    logger.debug("deleting declaration $id")
                    freeSlotDeclRepo.delete(id)
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                }
            }
        } else {
            logger.error("no user found")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PutMapping(value = "/declarations/{id}", produces = ["application/json"])
    fun updateFreeSlotDeclaration(@PathVariable("id") id: String, @RequestBody decl: FreeSlotDeclaration, principal: Principal): ResponseEntity<FreeSlotDeclaration> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            var decl=freeSlotDeclRepo.findOne(id)
            if(decl==null){
                logger.error("declaration $id not found")
                return return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }
            else{
                if(decl.owner!=user.login){
                    logger.error("unauthorized for user "+principal.name)
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    logger.debug("updating declaration $id")
                    decl.id=id
                    decl=freeSlotDeclRepo.save(decl);
                    return ResponseEntity.status(HttpStatus.OK).body(decl);
                }
            }
        } else {
            logger.error("no user found")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }


}