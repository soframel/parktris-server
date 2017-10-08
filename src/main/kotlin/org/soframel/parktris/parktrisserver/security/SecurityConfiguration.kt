package org.soframel.parktris.parktrisserver.security

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.config.annotation.web.builders.WebSecurity



@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    val userDetailsService: UserDetailsService? = null


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        //http.authorizeRequests().anyRequest().fullyAuthenticated()
        //http.httpBasic()
        //http.csrf().disable()
        http.authorizeRequests().antMatchers("/**").hasRole("BASIC").and().httpBasic();
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web!!
                .ignoring()
                .antMatchers("/unauth/**")
    }


    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        if(auth==null){
            throw Exception("AuthenticationManagerBuilder should not be null")
        }
        auth!!.authenticationProvider(authenticationProvider())
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }
}