package org.soframel.parktris.parktrisserver

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.authentication.NoAuthScheme
import io.restassured.http.ContentType
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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext
class HttpCreateUserTest : AbstractFongoTest() {

    @Autowired
    lateinit var appContext: ApplicationContext

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Value("\${local.server.port}")
    var port: Int = 0

    val adminPassword = "catalan"


	@Before
    fun setUp() {

        val admin = userRepository.findByLogin(userDetailsService.firstAdminLogin)
        admin.password = securityConfig.encoder().encode(adminPassword)
        userRepository.save(admin)


        RestAssured.port = port
    }

    /*Test creation of a user.
    * Currently not working, looks like you need to be authenticated to call /unauth/user.
    * */
    @Test
    fun testCreateUser() {
        //Create a new user anonymously
        val user = User()
        user.id = ""
        val lilysLogin="lily"
        val lilysEmail = "lily@test.lu"
        user.login=lilysLogin
        user.email = lilysEmail
        user.password = "something"

        val userJason = ObjectMapper().writeValueAsString(user)
        RestAssured.authentication = NoAuthScheme()
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userJason)
                .post("/unauth/user")
                .then()
                .statusCode(200).log().all();

        //Access to resources should be disabled even tho the user is existing
        RestAssured.authentication = RestAssured.basic(user.login, user.password)
        RestAssured.`when`().get("/loan")
                .then().statusCode(401)


        //Act as the admin and enable user
        val lily = userRepository.findByLogin(lilysLogin)
        lily.enabled = true
        userRepository.save(lily)

        //Access should be OK now
        RestAssured.authentication = RestAssured.basic(user.login, user.password)
        RestAssured.`when`().get("/")
                .then().statusCode(200)

   }





}