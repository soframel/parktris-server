package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.security.access.prepost.PreAuthorize

@RepositoryRestResource( exported = false)
interface UserRepository : MongoRepository<User, String> {
    fun findByLogin(@Param("login") login: String): User

}
