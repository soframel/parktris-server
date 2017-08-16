package org.soframel.parktris.parktrisserver

import org.junit.Test
import org.junit.runner.RunWith
import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@SpringBootTest
class ParktrisServerApplicationTests {

	@Autowired
	lateinit var appContext: ApplicationContext

	@Test
	fun contextLoads() {
        var areaRepo=appContext.getBean(ParkingAreaRepository::class.java)
		assertNotNull(areaRepo)
    }

    @Test
    fun tetstParkingAreaRepository() {
		var areaRepo=appContext.getBean(ParkingAreaRepository::class.java)

        areaRepo.deleteAll();

		var area = ParkingArea()
		area.name="-2"

		areaRepo.save(area)

		assertEquals(1, areaRepo.findAll().size)
    }

}
