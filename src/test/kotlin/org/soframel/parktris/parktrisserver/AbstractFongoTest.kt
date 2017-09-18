package org.soframel.parktris.parktrisserver

import org.junit.Rule
import org.soframel.parktris.parktrisserver.conf.RepositoryTestConfiguration
import org.springframework.context.ApplicationContext
import kotlin.test.assertTrue

open class AbstractFongoTest {

    @Rule
    @JvmField
    final var mongoDbRule = RepositoryTestConfiguration.getSpringMongoDbRule()


    constructor() {
        assertTrue(propertyByType(this), "Test missconfiguration. Please have @Autowired applicationContext member declared in test class. It's a SpringMongoDbRule limitation at the current version.")
    }



    /*
     * Copied with slight modifications from Fongo PropertyGetter.class v2.0.13 :
     * checks if a field of a certain type exists. The point is that the "disfunctionality" of not checking superclasses
     * is preserved.
     */
    fun propertyByType(testInstance: Any): Boolean {
        val clazz = testInstance.javaClass
        val fields = clazz.getDeclaredFields()
        val len = fields.size

        return (0 until len)
                .map { fields[it] }
                .any { it.type.isAssignableFrom(ApplicationContext::class.java) }
    }
}