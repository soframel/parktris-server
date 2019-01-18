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
    override fun toString(): String {
        return "ParkingSlot(id=$id, name=$name, desc=$desc, areaId=$areaId, owner=$owner)"
    }


}