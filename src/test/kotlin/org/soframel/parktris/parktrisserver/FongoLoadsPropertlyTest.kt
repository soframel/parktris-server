package org.soframel.parktris.parktrisserver

import com.mongodb.MockMongoClient
import org.junit.Test
import org.junit.runner.RunWith
import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertTrue


@RunWith(SpringRunner::class)
@SpringBootTest
class FongoLoadsPropertlyTest : AbstractFongoTest() {

    @Autowired
    lateinit var appContext: ApplicationContext


    @Autowired
    lateinit var parkingAreaRepository: ParkingAreaRepository


    @Test
    fun testFongoLoaded() {
        val bean = appContext.getBean("mongo")
        assertTrue(bean is MockMongoClient)
    }

    @Test
    fun testSimpleInsert() {
        parkingAreaRepository.count()
        parkingAreaRepository.insert(ParkingArea())
        assert(parkingAreaRepository.count() == 1L)
    }
}
