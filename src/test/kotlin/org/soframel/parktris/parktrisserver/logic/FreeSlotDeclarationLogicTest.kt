package org.soframel.parktris.parktrisserver.logic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.slf4j.LoggerFactory
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
class FreeSlotDeclarationLogicTest {

    var logger = LoggerFactory.getLogger(FreeSlotDeclarationLogicTest::class.java)

    @Autowired
    lateinit var declLogic: FreeSlotDeclarationLogic

    @Test
    fun testIsDeclarationAvailableWithOneLoan(){
        var decl=FreeSlotDeclaration()
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1= Loan()
        loan1.startDate= LocalDate.of(2019, 5, 1)
        loan1.endDate=LocalDate.of(2019, 5, 31)

        var loans= listOf<Loan>(loan1)

        assertFalse(declLogic.isDeclarationAvailable(decl, loans))
    }

    @Test
    fun testIsDeclarationAvailableWithOneLoanTrue(){
        var decl=FreeSlotDeclaration()
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1= Loan()
        loan1.startDate= LocalDate.of(2019, 5, 18)
        loan1.endDate=LocalDate.of(2019, 5, 19)

        var loans= listOf<Loan>(loan1)

        assertTrue(declLogic.isDeclarationAvailable(decl, loans))
    }


    @Test
    fun testIsDeclarationAvailableWithHoles(){
        var decl=FreeSlotDeclaration()
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1= Loan()
        loan1.startDate= LocalDate.of(2019, 5, 2)
        loan1.endDate=LocalDate.of(2019, 5, 10)

        var loan2= Loan()
        loan2.startDate= LocalDate.of(2019, 5, 20)
        loan2.endDate=LocalDate.of(2019, 5, 31)

        var loan3= Loan()
        loan3.startDate= LocalDate.of(2019, 5, 18)
        loan3.endDate=LocalDate.of(2019, 5, 22)


        var loans= listOf<Loan>(loan1, loan2, loan3)

        assertTrue(declLogic.isDeclarationAvailable(decl, loans))
    }

    @Test
    fun testIsDeclarationAvailableWithNoHoles(){
        var decl=FreeSlotDeclaration()
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1= Loan()
        loan1.startDate= LocalDate.of(2019, 5, 1)
        loan1.endDate=LocalDate.of(2019, 5, 10)

        var loan2= Loan()
        loan2.startDate= LocalDate.of(2019, 5, 10)
        loan2.endDate=LocalDate.of(2019, 5, 31)

        var loans= listOf<Loan>(loan1, loan2)

        assertFalse(declLogic.isDeclarationAvailable(decl, loans))
    }

    @Test
    fun testIsDeclarationAvailableWithNoLoan(){
        var decl=FreeSlotDeclaration()
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loans= listOf<Loan>()

        assertTrue(declLogic.isDeclarationAvailable(decl, loans))
    }


    @Test
    fun testGetDatesIntervalsFromDates(){
        var dates= listOf<LocalDate>(LocalDate.of(2019, 5, 1),
                LocalDate.of(2019, 5, 2),
                LocalDate.of(2019, 5, 3),
                LocalDate.of(2019, 5, 6),
                LocalDate.of(2019, 5, 7),
                LocalDate.of(2019, 5, 9),
                LocalDate.of(2019, 5, 10))
        var intervals=declLogic.getDateInvervalsFromDates(dates)
        var expected= listOf<DateInterval>(DateInterval(LocalDate.of(2019, 5, 1),LocalDate.of(2019, 5, 3) ),
                DateInterval(LocalDate.of(2019, 5, 6),LocalDate.of(2019, 5, 7)),
                DateInterval(LocalDate.of(2019, 5, 9),LocalDate.of(2019, 5, 10)))

        logger.debug("comparing expected = "+expected + "\nto intervals = "+ intervals)

        assertEquals(expected, intervals)
    }
    @Test
    fun testGetDataIntervalsFromDates_Complex(){
        var dates= listOf<LocalDate>(
                LocalDate.of(2019,5,6),
                LocalDate.of(2019,5,7),
                LocalDate.of(2019,5,8),
                LocalDate.of(2019,5,9),
                LocalDate.of(2019, 5,10),
                LocalDate.of(2019,5,11),
                LocalDate.of(2019,5,12),
                LocalDate.of(2019,5,16),
                LocalDate.of(2019,5,17),
                LocalDate.of(2019,5,18),
                LocalDate.of(2019,5,19),
                LocalDate.of(2019,5,20),
                LocalDate.of(2019,5,21),
                LocalDate.of(2019,5,27),
                LocalDate.of(2019,5,28),
                LocalDate.of(2019,5,29),
                LocalDate.of(2019,5,30),
                LocalDate.of(2019,5,31))

        var intervals=declLogic.getDateInvervalsFromDates(dates)
        var expected= listOf<DateInterval>(DateInterval(LocalDate.of(2019, 5, 6),LocalDate.of(2019, 5, 12) ),
                DateInterval(LocalDate.of(2019, 5, 16),LocalDate.of(2019, 5, 21)),
                DateInterval(LocalDate.of(2019, 5, 27),LocalDate.of(2019, 5, 31)))

        logger.debug("comparing expected = "+expected + "\nto intervals = "+ intervals)

        assertEquals(expected, intervals)
    }


    @Test
    fun testExtendFreeSlotDeclaration(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1=Loan()
        loan1.startDate=LocalDate.of(2019,5,1)
        loan1.endDate=LocalDate.of(2019,5,5)

        var loan2=Loan()
        loan2.startDate=LocalDate.of(2019,5,13)
        loan2.endDate=LocalDate.of(2019,5,15)

        var loan3=Loan()
        loan3.startDate=LocalDate.of(2019,5,22)
        loan3.endDate=LocalDate.of(2019,5,26)

        var loans=listOf<Loan>(loan1, loan2, loan3)
        var loanRepo=Mockito.mock(LoanRepository::class.java)
        declLogic.loanRepo=loanRepo
        Mockito.`when`(loanRepo.findAllByDeclId(ArgumentMatchers.anyString())).thenReturn(loans)

        var int1=DateInterval(LocalDate.of(2019,5,6), LocalDate.of(2019,5,12))
        var int2=DateInterval(LocalDate.of(2019,5,16), LocalDate.of(2019,5,21))
        var int3=DateInterval(LocalDate.of(2019,5,27), LocalDate.of(2019,5,31))
        var expected=listOf<DateInterval>(int1, int2, int3)

        var result=declLogic.extendFreeSlotDeclaration(decl)
        assertEquals(expected, result.availabilities)

    }

    @Test
    fun testExtendFreeSlotDeclaration2(){
        var decl=FreeSlotDeclaration()
        decl.id="42"
        decl.startDate= LocalDate.of(2019, 5, 1)
        decl.endDate= LocalDate.of(2019, 5, 31)

        var loan1=Loan()
        loan1.startDate=LocalDate.of(2019,5,18)
        loan1.endDate=LocalDate.of(2019,5,19)


        var loans=listOf<Loan>(loan1)
        var loanRepo=Mockito.mock(LoanRepository::class.java)
        declLogic.loanRepo=loanRepo
        Mockito.`when`(loanRepo.findAllByDeclId(ArgumentMatchers.anyString())).thenReturn(loans)

        var int1=DateInterval(LocalDate.of(2019,5,1), LocalDate.of(2019,5,17))
        var int2=DateInterval(LocalDate.of(2019,5,20), LocalDate.of(2019,5,31))
        var expected=listOf<DateInterval>(int1, int2)

        var result=declLogic.extendFreeSlotDeclaration(decl)
        assertEquals(expected, result.availabilities)

    }
}