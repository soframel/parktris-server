package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.Resources

class ParkingArea()  {
    @Id
    lateinit var id: String
    lateinit var name: String
    var desc: String?=null
}