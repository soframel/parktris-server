package org.soframel.parktris.parktrisserver.logic

import org.apache.log4j.Logger
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.soframel.parktris.parktrisserver.ParktrisServerApplication
import org.soframel.parktris.parktrisserver.repositories.LoanRepository
import org.soframel.parktris.parktrisserver.vo.DateInterval
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import kotlin.test.assertEquals


@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ParktrisServerApplication::class))
@DirtiesContext
class LoanLogicTest {

    var logger = Logger.getLogger(LoanLogicTest::class.java)

    @Autowired
    lateinit var loanLogic: LoanLogic


    @Test
    fun testIsValidLoan_IncorrectDates(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        var loan=Loan()
        loan.slotId="42"
        loan.startDate=LocalDate.of(2019,5,6)
        loan.endDate=LocalDate.of(2019,5,4)
        assertFalse(loanLogic.isValidLoan(loan, decl, "toto"))
    }

    @Test
    fun testIsValidLoan_DatesNotInDecl(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate=LocalDate.of(2019,5,4)
        decl.endDate=LocalDate.of(2019,5,6)
        var loan=Loan()
        loan.slotId="42"
        loan.startDate=LocalDate.of(2019,5,7)
        loan.endDate=LocalDate.of(2019,5,4)
        assertFalse(loanLogic.isValidLoan(loan, decl, "toto"))
    }

    @Test
    fun testIsValidLoan_IncorrectTenant(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.preferedTenant= mutableListOf<String>("toto", "titi")
        var loan=Loan()
        loan.slotId="42"
        loan.startDate=LocalDate.of(2019,5,6)
        loan.endDate=LocalDate.of(2019,5,4)
        assertFalse(loanLogic.isValidLoan(loan, decl, "tutu"))
    }

    @Test
    fun testIsValidLoan_Valid(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate=LocalDate.of(2019,5,4)
        decl.endDate=LocalDate.of(2019,5,6)
        decl.preferedTenant= mutableListOf<String>("toto", "titi")
        var loan=Loan()
        loan.slotId="42"
        loan.startDate=LocalDate.of(2019,5,4)
        loan.endDate=LocalDate.of(2019,5,6)
        assertTrue(loanLogic.isValidLoan(loan, decl, "toto"))
    }

    @Test
    fun testCheckLoanDatesOk(){
        var loanRepo=Mockito.mock(LoanRepository::class.java)

        var loan1=Loan()
        loan1.startDate=LocalDate.of(2019,5,1)
        loan1.endDate=LocalDate.of(2019,5,5)

        var loan2=Loan()
        loan2.startDate=LocalDate.of(2019,5,13)
        loan2.endDate=LocalDate.of(2019,5,15)

        var loan3=Loan()
        loan3.startDate=LocalDate.of(2019,5,22)
        loan3.endDate=LocalDate.of(2019,5,26)
        var loans= listOf<Loan>(loan1, loan2, loan3)
        Mockito.`when`(loanRepo.findAllByDeclId(anyString())).thenReturn(loans)

        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate= LocalDate.of(2019,5,1)
        decl.endDate= LocalDate.of(2019,5,31)

        var loan=Loan()
        loan.startDate=LocalDate.of(2019,5,16)
        loan.endDate=LocalDate.of(2019,5,21)

        assertTrue(loanLogic.checkLoanDates(decl, loan))
    }

    @Test
    fun testCheckLoanDatesNotOk(){
        var loanRepo=Mockito.mock(LoanRepository::class.java)
        loanLogic.loanRepo=loanRepo

        var loan1=Loan()
        loan1.startDate=LocalDate.of(2019,5,1)
        loan1.endDate=LocalDate.of(2019,5,5)

        var loan2=Loan()
        loan2.startDate=LocalDate.of(2019,5,13)
        loan2.endDate=LocalDate.of(2019,5,15)

        var loan3=Loan()
        loan3.startDate=LocalDate.of(2019,5,22)
        loan3.endDate=LocalDate.of(2019,5,26)
        var loans= listOf<Loan>(loan1, loan2, loan3)
        Mockito.`when`(loanRepo.findAllByDeclId(anyString())).thenReturn(loans)

        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate= LocalDate.of(2019,5,1)
        decl.endDate= LocalDate.of(2019,5,31)

        var loan=Loan()
        loan.startDate=LocalDate.of(2019,5,16)
        loan.endDate=LocalDate.of(2019,5,22)

        assertFalse(loanLogic.checkLoanDates(decl, loan))
    }
}