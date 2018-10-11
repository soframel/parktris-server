package org.soframel.parktris.parktrisserver.services

import org.soframel.parktris.parktrisserver.repositories.FreeSlotDeclarationRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import javax.websocket.server.PathParam

@RestController
class FreeSlotDeclarationService {
    @Autowired
    lateinit var freeSlotDeclRepo: FreeSlotDeclarationRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @PostMapping(value = "/declarations", produces = ["application/json"])
    fun createFreeSlotDeclaration(@RequestBody decl: FreeSlotDeclaration, principal: Principal): ResponseEntity<FreeSlotDeclaration> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            var start=decl.startDate
            var end=decl.endDate
            if(start!=null && end!=null && (start.isEqual(end) || start.isBefore(end))){
                decl.owner = user.login
                var result = freeSlotDeclRepo.save(decl)
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PreAuthorize("#owner == principal.username")
    @GetMapping(value = "/declarations", produces= ["application/json"])
    fun getDeclarationsFromOwner(@RequestParam("owner") owner: String, principal: Principal): ResponseEntity<List<FreeSlotDeclaration>> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
            var userId=user.id
            if(userId!=null) {
                var result = freeSlotDeclRepo.findAllByOwner(userId)
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping(value = "/declarations/{id}")
    fun deleteFreeSlotDeclaration(@PathVariable("id") id: String, pincipal: Principal): ResponseEntity<Void> {
        var user = userRepo.findByLogin(pincipal.name)
        if (user != null) {
            var decl=freeSlotDeclRepo.findOne(id)
            if(decl==null){
                return return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            else{
                if(decl.owner!=user.login){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    freeSlotDeclRepo.delete(id)
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PutMapping(value = "/declarations/{id}", produces = ["application/json"])
    fun updateFreeSlotDeclaration(@PathVariable("id") id: String, @RequestBody decl: FreeSlotDeclaration, pincipal: Principal): ResponseEntity<FreeSlotDeclaration> {
        var user = userRepo.findByLogin(pincipal.name)
        if (user != null) {
            var decl=freeSlotDeclRepo.findOne(id)
            if(decl==null){
                return return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            else{
                if(decl.owner!=user.login){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    decl.id=id
                    decl=freeSlotDeclRepo.save(decl);
                    return ResponseEntity.status(HttpStatus.OK).body(decl);
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }


}