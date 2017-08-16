package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id

class Owner () {
    @Id
    lateinit var userId: String
    var ownedSlotsIds: MutableList<String>?=null
}