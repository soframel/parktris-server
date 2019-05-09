package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.lang.Nullable;

interface ParkingSlotRepository : MongoRepository<ParkingSlot, String> {

    @Nullable
    fun findByName(@Param("name") name: String): ParkingSlot
    fun findAllByIdIsNotNull(): List<ParkingSlot>
    fun findAllByOwner(@Param("owner") owner: String): List<ParkingSlot>
}