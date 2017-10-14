package org.soframel.parktris.parktrisserver

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.security.UserDetailsService
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import java.util.Random


@Component
class InitialConfigurator {

    var logger = Logger.getLogger(InitialConfigurator::class.java)

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {

        if (userRepo.findByLogin(userDetailsService.firstAdminLogin) == null) {
            logger.warn("No admin user - generating one")
            var pass = generateRandom()
            logger.warn("password is '${pass}'")
            var user = User()
            user.id = null //let it be auto generated
            user.login=userDetailsService.firstAdminLogin
            user.email = userDetailsService.firstAdminEmail
            user.fullName="Administrator"
            user.enabled=true
            user.password=securityConfig.encoder().encode(pass)
            userRepo.insert(user)
        }
    }


    fun generateRandom(): String {
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