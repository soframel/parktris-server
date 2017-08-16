package org.soframel.parktris.parktrisserver

import org.soframel.parktris.parktrisserver.services.UserService
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    val userService: UserService? = null

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        if(auth==null){
            throw Exception("AuthenticationManagerBuilder should not be null")
        }
        auth.userDetailsService<UserService>(userService)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().anyRequest().fullyAuthenticated()
        http.httpBasic()
        http.csrf().disable()
        
    }
}