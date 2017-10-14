package org.soframel.parktris.parktrisserver.conf

import org.soframel.parktris.parktrisserver.PasswordGenerator

class StaticPasswordGenerator : PasswordGenerator {
    override fun generate(): String {
        return "doyoubelieveitnowneo"
    }

}