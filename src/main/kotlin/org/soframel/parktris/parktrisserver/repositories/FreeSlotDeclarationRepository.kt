package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.soframel.parktris.parktrisserver.vo.ParkingSlot
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.security.access.prepost.PreAuthorize
import java.util.*

interface FreeSlotDeclarationRepository : MongoRepository<FreeSlotDeclaration, String> {

    fun findAllByOwner(@Param("owner") owner: String): List<FreeSlotDeclaration>

    /**
     * TODO: also check that slot declaration is not already reserved by >=1 loans
     */
    @Query("{\"endDate\":{ \"\$gte\": ?0}}")
    fun findAllAvailableFreeSlotsBeforeDate(date: Date): List<FreeSlotDeclaration>

    @Query("{\"owner\": ?0, \"endDate\":{ \"\$gte\": ?1}}")
    fun findFutureByOwner(owner: String, date: Date): List<FreeSlotDeclaration>
}