package org.soframel.parktris.parktrisserver

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener


@Component
class InitialConfigurator : InitializingBean {

    var logger = Logger.getLogger(InitialConfigurator::class.java)

    @Autowired
    lateinit var userRepo: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var passwordGenerator : PasswordGenerator

    @Value("\${parktris.server.admins}")
    lateinit var adminEmail: String

    override fun afterPropertiesSet() {
        if (adminEmail.contains(",", false)) {
            logger.info("cropped admin email to ${adminEmail}")
            adminEmail = adminEmail.substring(0, adminEmail.indexOf(",")).trim()
        }
    }


    @EventListener(ContextRefreshedEvent::class)
    fun contextRefreshedEvent() {

        if (userRepo.findByEmail(adminEmail) == null) {
            logger.warn("No admin user - generating one")
            var pass = passwordGenerator.generate()
            logger.warn("password is '${pass}'")
            var user = User()
            user.id = null //let it be auto generated
            user.email = adminEmail
            user.fullName="Administrator"
            user.enabled=true
            user.password=securityConfig.encoder().encode(pass)
            userRepo.insert(user)
        }
    }


}