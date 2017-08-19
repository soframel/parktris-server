package org.soframel.parktris.parktrisserver.services

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.InitialConfigurator
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@RestController
class UserMgtService{

    var logger = Logger.getLogger(UserMgtService::class.java)

    @Autowired
    lateinit var secu: SecurityConfiguration

    @Autowired
    lateinit var userRepo: UserRepository

    @RequestMapping(value="/userMgt/{email}", method = arrayOf(RequestMethod.GET))
    fun findByEmail(@PathVariable email: String): User{
        return userRepo.findByEmail(email)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/userMgt/enableUser/{email}", method = arrayOf(RequestMethod.PUT))
    fun enableUser(@PathVariable email: String): ResponseEntity<String>{
          var user=userRepo.findByEmail(email)
        if(user!=null){
            user.enabled=true
            val user2=userRepo.save(user)
            return ResponseEntity.status(HttpStatus.OK).body("User with email ${email} enabled")
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found with this email: ${email}")
        }
    }

    @RequestMapping(value="/unauth/user", method=arrayOf(RequestMethod.POST))
    fun createUser(@RequestBody user: User): ResponseEntity<String>{
        logger.info("creating user ${user}")
        if(user.email==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User must have an email")
        }
        else if(userRepo.findByEmail(user.email!!)!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with email ${user.email} already exists")
        }
        else {
            /*var user = User()
            user.email = email
            user.fullName=fullName;
            user.password = secu.encoder().encode(password)  */
            user.password= secu.encoder().encode(user.password)
            user.enabled=false
            var user2 = userRepo.save(user)
            return ResponseEntity.status(HttpStatus.OK).body("User with email ${user.email} created. It must now be enabled by an administrator")
        }
    }
}