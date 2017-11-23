package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources

class ParkingArea()  {
    @Id
    var id: String?=null
    var name: String?=null
    var desc: String?=null
}