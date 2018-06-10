package org.soframel.parktris.parktrisserver

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.Resources
import org.springframework.hateoas.client.Traverson
import java.net.URI
import org.soframel.parktris.parktrisserver.repositories.ParkingSlotRepository
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.soframel.parktris.parktrisserver.vo.wrapped.ParkingAreaResource
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpMethod
import org.soframel.parktris.parktrisserver.vo.wrapped.ParkingSlotResource
import org.springframework.hateoas.Resource
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import java.util.HashMap
import javax.validation.constraints.AssertTrue
import kotlin.test.assertEquals


@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HttpParkingSlotDeclarationTest : AbstractFongoTest() {

    @Autowired
    lateinit var appContext: ApplicationContext


    @Value("\${local.server.port}")
    var port: Int = 0

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var parkingAreaRepo: ParkingAreaRepository

    @Autowired
    lateinit var parkingSlotRepo: ParkingSlotRepository

    lateinit var username : String
    lateinit var password : String
    lateinit var serverUrl : String

    lateinit var user: User
    lateinit var adminUser: User

    @Before
    fun setUp() {

        adminUser=userRepository.findByLogin("admin")

        username = "mila"
        password = "piplu"
        serverUrl = "http://localhost:" + port

        user = User()
        user.login=username
        user.email = username
        user.password = securityConfig.encoder().encode(password)
        user.enabled = true
        userRepository.save(user)

        var parkingArea2 = ParkingArea()
        parkingArea2.name = "-2"
        parkingAreaRepo.insert(parkingArea2)
        var parkingArea25 = ParkingArea()
        parkingArea25.name = "-2.5"
        parkingAreaRepo.insert(parkingArea25)


        val slot = ParkingSlot()
        slot.name = "JL"
        slot.areaId = parkingArea25.id
        slot.owner=user.login
        parkingSlotRepo.insert(slot)

        val slot2 = ParkingSlot()
        slot2.name = "Pat"
        slot2.areaId = parkingArea25.id
        slot2.owner=adminUser.login
        parkingSlotRepo.insert(slot2)

        RestAssured.port = port
    }

    /*Test creation of a parkingSpot.*/
    @Test
    fun testDeclareParkingSlot() {

        //get all areas from HTTP call
        val allAreas = Traverson(URI(serverUrl), MediaTypes.HAL_JSON)
                .follow("parkingArea", "search", "findAllByIdIsNotNull")
                .withTemplateParameters(HashMap())
                .withHeaders(getHttpHeaders(username, password))
                .toObject(object : ParameterizedTypeReference<Resources<ParkingAreaResource>>() {})

        //get one parking area. There's something weird when I do allAreas.content as ArrayList, so I neeed to iterate
        var area = ParkingArea()
        for (parkingAreaResource in allAreas.content) {
            area = parkingAreaResource.parkingArea;
        }

        //create new parking slot
        area.id = "" //need to set something because of lateinit. anyway it should be overridden

        val slot = ParkingSlot()
        slot.areaId = area.id
        slot.desc = "038"
        slot.name = "Dave"
        slot.owner=user.login
        slot.id = ""

        val slotResource = ParkingSlotResource()
        slotResource.parkingSlot = slot

        val requestPayload = HashMap<String, Any>()
        requestPayload.put("parkingSlot", slot)
        val payload = ObjectMapper().writeValueAsString(slotResource)

        val request = HttpEntity<String>(payload, getHttpHeaders(username, password))
        val response = RestTemplate().postForEntity(serverUrl + "parkingSlot", request, String::class.java)

        //check that the slot is there
        val paramsForDavesSlot = HashMap<String, Any>()
        paramsForDavesSlot.put("name", "Dave");
        val davesSlot = Traverson(URI(serverUrl), MediaTypes.HAL_JSON)
                .follow("parkingSlot", "search", "findByName")
                .withTemplateParameters(paramsForDavesSlot)
                .withHeaders(getHttpHeaders(username, password))
                .toObject(object : ParameterizedTypeReference<Resource<ParkingSlotResource>>() {})
        assertEquals(davesSlot.content.parkingSlot.desc, "038");

    }


    private fun getHttpHeaders(username: String, password: String): HttpHeaders {
        val plainCreds = username + ":" + password
        val plainCredsBytes = plainCreds.toByteArray()
        val base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes)
        val base64Creds = String(base64CredsBytes)

        val headers = HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds)
        return headers
    }


}