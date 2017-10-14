package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import org.springframework.hateoas.ResourceSupport

class ParkingSlot(){
    lateinit var id: String
    lateinit var name: String
    var desc: String?=""
    lateinit var area: ParkingArea
}