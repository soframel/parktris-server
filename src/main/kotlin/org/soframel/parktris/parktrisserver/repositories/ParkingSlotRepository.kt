package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "parkingSlot", path = "parkingSlot")
interface ParkingSlotRepository : MongoRepository<ParkingSlot, String> {
    fun findByName(@Param("name") name: String): ParkingSlot
    fun findAllByIdIsNotNull(): List<ParkingSlot>
}