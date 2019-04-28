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
import java.security.Principal

@RestController
class UserMgtService{

    var logger = Logger.getLogger(UserMgtService::class.java)

    @Autowired
    lateinit var secu: SecurityConfiguration

    @Autowired
    lateinit var userRepo: UserRepository

    @RequestMapping(value="/userMgt/{login}", method = arrayOf(RequestMethod.GET))
    fun findByLogin(@PathVariable login: String): User{
        return userRepo.findByLogin(login)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/userMgt/enableUser/{login}", method = arrayOf(RequestMethod.PUT))
    fun enableUser(@PathVariable login: String): ResponseEntity<String>{
          var user=userRepo.findByLogin(login)
        if(user!=null){
            user.enabled=true
            val user2=userRepo.save(user)
            return ResponseEntity.status(HttpStatus.OK).body("User with login ${login} enabled")
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found with this login: ${login}")
        }
    }

    @RequestMapping(value="/unauth/user", method=arrayOf(RequestMethod.POST))
    fun createUser(@RequestBody user: User): ResponseEntity<String>{
        logger.info("creating user ${user}")
        if(user.login==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User must have a login")
        }
        else if(userRepo.findByLogin(user.login!!)!=null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with login ${user.login} already exists")
        }
        else {
            // our friend Spring should already have encrypted user.password at this point, so don't change it
            user.id = null //set to null to let it be generated to prevent overriding on purpose
            user.enabled=false
            userRepo.insert(user)
            return ResponseEntity.status(HttpStatus.OK).body("User with login ${user.login} created. It must now be enabled by an administrator")
        }
    }

    /** Want Slot **/

    @RequestMapping(value="/users/wantSlot/{login}", method = arrayOf(RequestMethod.GET))
    fun doesUserWantSlot(@PathVariable login: String, principal: Principal): ResponseEntity<Boolean>{
        if(login==null || !login.equals(principal.name)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false)
        }
        else {
            val user = userRepo.findByLogin(principal.name)
            return  ResponseEntity.status(HttpStatus.OK).body(user.wantSlot)
        }
    }
    @RequestMapping(value="/users/wantSlot/{login}", method = arrayOf(RequestMethod.PUT))
    fun setUserWantSlot(@PathVariable login: String, @RequestParam want: Boolean, principal: Principal): ResponseEntity<Void>{
        if(login==null || !login.equals(principal.name)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        else {
            logger.info("setUserWantSlot for "+principal.name+" to "+want);
            val user = userRepo.findByLogin(principal.name)
            user.wantSlot = want
            userRepo.save(user)
            return  ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }
}