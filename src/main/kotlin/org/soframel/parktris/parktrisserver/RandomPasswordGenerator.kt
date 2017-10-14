package org.soframel.parktris.parktrisserver

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class RandomPasswordGenerator : PasswordGenerator {
    override fun generate(): String {
        val leftLimit = 48 // number '0'
        val rightLimit = 122 // letter 'z'
        val targetStringLength = 10
        val random = Random()
        val buffer = StringBuilder(targetStringLength)
        for (i in 0..targetStringLength - 1) {
            val randomLimitedInt = leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
            buffer.append(randomLimitedInt.toChar())
        }
        return buffer.toString()
    }
}