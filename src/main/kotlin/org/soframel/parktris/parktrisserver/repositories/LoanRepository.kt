package org.soframel.parktris.parktrisserver.repositories

import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.soframel.parktris.parktrisserver.vo.ParkingArea
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

interface LoanRepository : MongoRepository<Loan, String> {

    fun findAllByDeclId(@Param("declId") declId: String): List<Loan>
}