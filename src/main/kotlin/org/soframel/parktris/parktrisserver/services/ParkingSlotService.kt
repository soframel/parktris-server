package org.soframel.parktris.parktrisserver.services

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.ParkingSlotRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class ParkingSlotService {

    var logger = Logger.getLogger(ParkingSlotService::class.java)

    @Autowired
    lateinit var parkingSlotRepo: ParkingSlotRepository

    @Autowired
    lateinit var userRepo: UserRepository

    @PostMapping(value = "/slots", produces = ["application/json"])
    fun createParkingSlot(@RequestBody slot: ParkingSlot, principal: Principal): ResponseEntity<ParkingSlot> {
        var user = userRepo.findByLogin(principal.name)
        if (user != null) {
                slot.owner = user.login
                var result = parkingSlotRepo.save(slot)
                return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PreAuthorize("#owner == principal.username")
    @GetMapping(value = "/slots", produces= ["application/json"])
    fun getSlotsFromOwner(@RequestParam("owner") owner: String, principal: Principal): ResponseEntity<List<ParkingSlot>> {
        var user = userRepo.findByLogin(owner)
        if (user != null) {
            if(owner!=null) {
                var result = parkingSlotRepo.findAllByOwner(owner)
                logger.debug("found slots by owner "+owner+": "+result)
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @DeleteMapping(value = "/slots/{id}")
    fun deleteParkingSlot(@PathVariable("id") id: String, pincipal: Principal): ResponseEntity<Void> {
        var user = userRepo.findByLogin(pincipal.name)
        if (user != null) {
            var slot=parkingSlotRepo.findOne(id)
            if(slot==null){
                return return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            else{
                if(slot.owner!=user.login){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    parkingSlotRepo.delete(id)
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }

    @PutMapping(value = "/slots/{id}", produces = ["application/json"])
    fun updateParkingSlot(@PathVariable("id") id: String, @RequestBody slot: ParkingSlot, pincipal: Principal): ResponseEntity<ParkingSlot> {
        var user = userRepo.findByLogin(pincipal.name)
        if (user != null) {
            var decl=parkingSlotRepo.findOne(id)
            if(decl==null){
                return return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            else{
                if(decl.owner!=user.login){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                else{
                    decl.id=id
                    decl=parkingSlotRepo.save(decl);
                    return ResponseEntity.status(HttpStatus.OK).body(decl);
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    }


}