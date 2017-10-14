package org.soframel.parktris.parktrisserver.security

import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService : UserDetailsService, InitializingBean {

    var logger = Logger.getLogger(UserDetailsService::class.java)

    @Autowired
    lateinit var userRepo: UserRepository

    @Value("#{'\${parktris.server.adminEmails}'.split(',')}")
    lateinit var adminEmails: List<String>

    @Value("#{'\${parktris.server.adminLogins}'.split(',')}")
    lateinit var adminLogins: List<String>

    lateinit var firstAdminEmail: String
    
    lateinit var firstAdminLogin: String

    override fun afterPropertiesSet() {
        if(adminEmails!=null && !adminEmails.isEmpty()){
              firstAdminEmail=adminEmails.get(0)
        }
        if(adminLogins!=null && !adminLogins.isEmpty()){
            firstAdminLogin =adminLogins.get(0)
        }
    }

    override fun loadUserByUsername(username: String): UserDetails
     {
        var user = userRepo.findByLogin(username)
        if (user == null) {
            throw UsernameNotFoundException("Username " + username + " not found")
        }
         //check user is enabled
         if(user.enabled) {
             return org.springframework.security.core.userdetails.User(username, user.password, getGrantedAuthorities(username))
         }
         else{
             throw UsernameNotFoundException("User is not enabled: ${username}")
         }
    }


    fun getGrantedAuthorities(username: String): Collection<GrantedAuthority> {
        var authorities: Collection<GrantedAuthority>
        if (adminLogins.contains(username)) {
            authorities = listOf<SimpleGrantedAuthority>(SimpleGrantedAuthority(true),SimpleGrantedAuthority(false))
        } else {
            authorities = listOf<SimpleGrantedAuthority>(SimpleGrantedAuthority(false))
        }
        return authorities;
    }
}

class SimpleGrantedAuthority(var admin: Boolean): GrantedAuthority{

    override fun getAuthority(): String {
        if(admin){
             return "ROLE_ADMIN"
        }
        else{
            return "ROLE_BASIC"
        }
    }
}