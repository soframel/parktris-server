package org.soframel.parktris.parktrisserver

//import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@SpringBootApplication
@EnableMongoRepositories
class ParktrisServerApplication: SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(ParktrisServerApplication::class.java!!)
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(ParktrisServerApplication::class.java, *args)
}
