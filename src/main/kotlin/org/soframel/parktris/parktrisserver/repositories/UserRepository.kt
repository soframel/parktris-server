package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
interface UserRepository : MongoRepository<User, String> {
    fun findByEmail(@Param("email") email: String): User
}
