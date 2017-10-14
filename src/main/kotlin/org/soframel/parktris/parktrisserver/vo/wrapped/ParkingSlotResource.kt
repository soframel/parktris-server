package org.soframel.parktris.parktrisserver.vo.wrapped

import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.springframework.hateoas.ResourceSupport

class ParkingSlotResource : ResourceSupport() {
    @JsonUnwrapped
    var parkingSlot = ParkingSlot()

}