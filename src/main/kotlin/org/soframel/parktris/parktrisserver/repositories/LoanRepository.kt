package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.Loan
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*

interface LoanRepository : MongoRepository<Loan, String> {

    fun findAllByDeclId(@Param("declId") declId: String): List<Loan>

    fun findAllByTenant(@Param("tenant") tenant: String): List<Loan>

    @Query("{\"endDate\":{ \"\$gte\": ?0}, \"tenant\": {\"\$eq\": ?1}}")
    fun findFutureLoansByTenant(date: Date, tenant: String): List<Loan>
}