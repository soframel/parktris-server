package org.soframel.parktris.parktrisserver

import org.slf4j.LoggerFactory
import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.soframel.parktris.parktrisserver.repositories.ParkingSlotRepository
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.security.SecurityConfiguration
import org.soframel.parktris.parktrisserver.security.UserDetailsService
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


/**
 * insert dummy data in DB in order to ease manual tests.
 * any resemblance to existing parking places is unintentional :-)
 *
 * to use, pass "exampleData" as command line argument
 * (with maven spring boot plugin, add -Drun.arguments=exampleData to command line)
 */
@Component
class ExampleDataInserter : CommandLineRunner {
    var logger = LoggerFactory.getLogger(ExampleDataInserter::class.java)

    override fun run(vararg args: String?) {
        if (args != null && args.contains("exampleData")) {
            logger.info("############# Inserting Example Data")
            this.insertData()
        }
    }

    @Autowired
    lateinit var securityConfig: SecurityConfiguration

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    @Autowired
    lateinit var parkingAreaRepo: ParkingAreaRepository

    @Autowired
    lateinit var parkingSlotRepo: ParkingSlotRepository

    //data
    var adminUser = User()
    var user = User()
    var parkingArea2 = ParkingArea()
    var parkingArea25 = ParkingArea()
    var parkingArea3 = ParkingArea()
    var parkingArea4 = ParkingArea()
    val slot = ParkingSlot()
    val slot2 = ParkingSlot()
    val slot3 = ParkingSlot()

    fun insertData() {
        this.insertUsers()
        this.insertParkingAreas()
        this.insertParkingSlots()
    }

    fun insertUsers() {
        userRepository.deleteAll()
        
        adminUser.login = userDetailsService.firstAdminLogin
        adminUser.email = userDetailsService.firstAdminEmail
        adminUser.fullName="Administrator"
        adminUser.password = securityConfig.encoder().encode("1mdpAdm")
        adminUser.enabled = true
        adminUser=userRepository.insert(adminUser)
        logger.info("adminUser created with id "+adminUser.id)

        user.login = "mila"
        user.email = "mila@test.lu"
        user.password = securityConfig.encoder().encode("piplu")
        user.enabled = true
        user=userRepository.insert(user)
        logger.info("user created with id "+user.id)
    }

    fun insertParkingAreas() {
        parkingAreaRepo.deleteAll()

        parkingArea2.name = "-2"
        parkingArea2.desc = "underground -2"
        parkingArea2=parkingAreaRepo.insert(parkingArea2)

        parkingArea25.name = "-2.5"
        parkingAreaRepo.insert(parkingArea25)

        parkingArea3.name = "-3"
        parkingAreaRepo.insert(parkingArea3)

        parkingArea4.name = "P4"
        parkingAreaRepo.insert(parkingArea4)
    }

    fun insertParkingSlots() {
        parkingSlotRepo.deleteAll()

        slot.name = "1"
        slot.areaId = parkingArea2.id
        slot.desc = "JL"
        slot.owner=user.login
        parkingSlotRepo.insert(slot)


        slot2.name = "68"
        slot2.desc = "Pat"
        slot2.owner=user.login
        slot2.areaId = parkingArea25.id
        parkingSlotRepo.insert(slot2)

        slot3.name = "42"
        slot3.desc = "Ford"
        slot3.owner=adminUser.login
        slot3.areaId = parkingArea3.id
        parkingSlotRepo.insert(slot3)

    }

}