package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.Loan
import org.soframel.parktris.parktrisserver.vo.Owner
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "owner", path = "owner")
interface OwnerRepository : MongoRepository<Owner, String> {

}