package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id

class Owner () {
    @Id
    var userId: String=""
    var ownedSlotsIds: MutableList<String> = mutableListOf()
}