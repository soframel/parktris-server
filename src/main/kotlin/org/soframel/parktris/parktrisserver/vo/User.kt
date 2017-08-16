package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id
import org.springframework.data.rest.core.annotation.RestResource

class User() {
    @Id var id: String=""
    lateinit var email: String

    @RestResource(exported = false)
    lateinit var password: String

    var enabled: Boolean=false
}