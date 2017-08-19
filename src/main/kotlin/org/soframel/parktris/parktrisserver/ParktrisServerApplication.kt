package org.soframel.parktris.parktrisserver

//import org.slf4j.LoggerFactory
import org.apache.log4j.Logger
import org.soframel.parktris.parktrisserver.repositories.UserRepository
import org.soframel.parktris.parktrisserver.vo.User
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import java.nio.charset.Charset
import java.util.*
import java.util.Random


@SpringBootApplication
@EnableMongoRepositories
class ParktrisServerApplication{
    

}

fun main(args: Array<String>) {
    SpringApplication.run(ParktrisServerApplication::class.java, *args)
}
