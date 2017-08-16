package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id

class ParkingSlot() {
    @Id
    lateinit var id: String
    lateinit var name: String
    var desc: String?=""
    lateinit var area: ParkingArea
}