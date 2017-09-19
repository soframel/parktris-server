package org.soframel.parktris.parktrisserver.conf

import com.github.fakemongo.Fongo
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfiguration
import com.lordofthejars.nosqlunit.mongodb.SpringMongoDbRule
import com.mongodb.MockMongoClient
import com.mongodb.Mongo
import org.soframel.parktris.parktrisserver.ParktrisServerApplication
import org.soframel.parktris.parktrisserver.repositories.ParkingAreaRepository
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment


/*
 *Configures Fongo to run instead of Mongo
 */
@Configuration
@EnableMongoRepositories
@ComponentScan(basePackageClasses = arrayOf(ParkingAreaRepository::class))
@PropertySource("classpath:application.properties")
class RepositoryTestConfiguration : AbstractMongoConfiguration() {

    @Autowired
    lateinit var env: Environment

    override fun getDatabaseName(): String {
        return  return env.getProperty("spring.data.mongodb.database")
    }

    @Bean
    override fun mongo(): Mongo {
        return Fongo(databaseName).getMongo()
    }

    override fun getMappingBasePackage(): String {
        return ParktrisServerApplication::class.java.getPackage().toString()
    }


    companion object {

        fun getSpringMongoDbRule(): SpringMongoDbRule {
            val mongoDbConfiguration = MongoDbConfiguration()
            mongoDbConfiguration.databaseName = "Test" //not sure whether this needs to be the same name as in getDatabaseName()
            val mongo = MockMongoClient.create(Fongo("Test")) //not sure whether this needs to be the same name as in getDatabaseName()
            mongoDbConfiguration.mongo = mongo
            return SpringMongoDbRule(mongoDbConfiguration)
        }
    }

}