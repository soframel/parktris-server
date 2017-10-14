package org.soframel.parktris.parktrisserver.vo

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.soframel.parktris.parktrisserver.security.BCryptPasswordDeserializer
import org.springframework.data.annotation.Id
import org.springframework.data.rest.core.annotation.RestResource

class User  {
    
    @Id var id: String?=""
    var login: String?=""
    var email: String?=""

    var fullName: String?=""

    @RestResource(exported = false)
    @JsonDeserialize(using = BCryptPasswordDeserializer::class )
    var password: String?=""

    var enabled: Boolean=false
    override fun toString(): String {
        return "User(id='$id', login='$login', email='$email', fullName='$fullName', password='$password', enabled=$enabled)"
    }


}