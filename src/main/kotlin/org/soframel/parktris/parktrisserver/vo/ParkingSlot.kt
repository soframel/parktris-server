package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id

class ParkingSlot() {
    @Id
    var id: String=""
    var name: String=""
    var desc: String?=""
    var area: ParkingArea=ParkingArea()
}