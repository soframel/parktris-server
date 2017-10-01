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
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpSecurityTest : AbstractFongoTest() {

    @Autowired
    lateinit var appContext: ApplicationContext

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Value("\${local.server.port}")
    var port: Int = 0

    @Value("\${parktris.server.admins}")
    lateinit var adminEmail: String

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
        user.email = "user@test.lu"
        user.password=securityConfig.encoder().encode(passwuert)
        user.enabled=true
        userRepository.save(user)
        RestAssured.authentication = basic(user.email, passwuert)
        `when`().get("/")
                .then().statusCode(200)

    }

    /*Test Admin access OK*/
    @Test
    fun testAdminAccess(){
        val admin = userRepository.findByEmail(adminEmail)
        admin.password = securityConfig.encoder().encode(passwuert)
        userRepository.save(admin)

        RestAssured.authentication = RestAssured.basic(adminEmail, passwuert)
        RestAssured.`when`().get("/")
                .then().statusCode(200)
    }

}