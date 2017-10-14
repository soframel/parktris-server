package org.soframel.parktris.parktrisserver

import io.restassured.RestAssured
import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.basic
import io.restassured.authentication.NoAuthScheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.security.UserDetailsService
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
class HttpSecurityTest : AbstractFongoTest() {

    @Autowired
    lateinit var appContext: ApplicationContext

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var passwordGenerator : PasswordGenerator

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Value("\${local.server.port}")
    var port: Int = 0

    lateinit var user : User
    var passwuert = "weTypeCodeNotArt"

	@Before
    fun setUp() {
        RestAssured.port = port
    }

    /*Test access denied if not authenticated*/
    @Test
	fun testBaseUrlSecu() {
        RestAssured.authentication = NoAuthScheme()
        `when`().get("/")
                .then().statusCode(401)

    }

    /*Test access OK if authenticated user*/
    @Test
    fun testUserAccess() {
        user = User()
        user.login = "user"
        user.email = "user@test.lu"
        user.password=securityConfig.encoder().encode(passwuert)
        user.enabled=true
        userRepository.save(user)
        RestAssured.authentication = basic(user.login, passwuert)
        `when`().get("/")
                .then().statusCode(200)

    }

    /*Test access NOK if authenticated but not enabled user */
    @Test
    fun testDisabledUserAccess() {
        user = User()
        user.email = "user2@test.lu"
        user.password=securityConfig.encoder().encode(passwuert)
        user.enabled=false
        userRepository.save(user)
        RestAssured.authentication = basic(user.email, passwuert)
        `when`().get("/")
                .then().statusCode(401)

    }

    /*Test Admin access OK*/
    @Test
    fun testAdminAccess(){

        RestAssured.authentication = RestAssured.basic(userDetailsService.firstAdminLogin, passwordGenerator.generate())

        val admin = userRepository.findByLogin(userDetailsService.firstAdminLogin)
        admin.password = securityConfig.encoder().encode(passwuert)
        userRepository.save(admin)

        RestAssured.authentication = RestAssured.basic(userDetailsService.firstAdminLogin, passwuert)

        RestAssured.`when`().get("/")
                .then().statusCode(200)
    }

}