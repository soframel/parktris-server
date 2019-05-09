package org.soframel.parktris.parktrisserver.controllers

import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class ParkingAreaController {
    @Autowired
    lateinit var parkingAreaRepository: ParkingAreaRepository

    @GetMapping(value = ["/areas"], produces= ["application/json"])
    fun getDeclarationsFromOwner(principal: Principal): ResponseEntity<List<ParkingArea>> {

                var result = parkingAreaRepository.findAllByIdIsNotNull();
                return ResponseEntity.status(HttpStatus.OK).body(result);

    }



}