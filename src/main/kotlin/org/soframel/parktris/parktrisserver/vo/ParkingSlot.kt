package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import org.springframework.hateoas.ResourceSupport

class ParkingSlot(){
    @Id
    var id: String?=null
    var name: String?=""
    var desc: String?=""
    var areaId: String?=null
    var owner: String?=null
}