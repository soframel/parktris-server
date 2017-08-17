package org.soframel.parktris.parktrisserver.security

import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepo: UserRepository

    @Value("\${parktris.server.admins}")
    lateinit var adminLogins: String

    fun login(email: String, password: String): Boolean{
        var user=userRepo.findByEmail(email)
       return false
    }


    override fun loadUserByUsername(username: String): UserDetails
     {
        var user = userRepo.findByEmail(username)
        if (user == null) {
            throw UsernameNotFoundException("Username " + username + " not found")
        }
        return org.springframework.security.core.userdetails.User(username, "password", getGrantedAuthorities(username))
    }


    fun getGrantedAuthorities(username: String): Collection<GrantedAuthority> {
        var authorities: Collection<GrantedAuthority>
        if (adminLogins.contains(username, true)) {
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