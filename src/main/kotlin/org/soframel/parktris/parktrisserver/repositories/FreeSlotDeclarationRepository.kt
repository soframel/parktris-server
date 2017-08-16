package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "freeSlotDeclaration", path = "freeSlotDeclaration")
interface FreeSlotDeclarationRepository : MongoRepository<FreeSlotDeclaration, String> {

}