package org.soframel.parktris.parktrisserver.vo

import org.springframework.data.annotation.Id

class User() {
    @Id var id: String=""
    var username: String=""
    var password: String=""
}