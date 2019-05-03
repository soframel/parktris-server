package org.soframel.parktris.parktrisserver.logic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.soframel.parktris.parktrisserver.ParktrisServerApplication
import org.soframel.parktris.parktrisserver.vo.FreeSlotDeclaration
import org.soframel.parktris.parktrisserver.vo.Loan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate


@ActiveProfiles("test")
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ParktrisServerApplication::class))
@DirtiesContext
class FreeSlotDeclarationLogicTest {

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
}