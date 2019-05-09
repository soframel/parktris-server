package org.soframel.parktris.parktrisserver

import org.slf4j.LoggerFactory
import org.soframel.parktris.parktrisserver.logic.FreeSlotDeclarationLogic
import org.soframel.parktris.parktrisserver.logic.LoanLogic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@SpringBootApplication
@EnableMongoRepositories
@Configuration
class ParktrisServerApplication: SpringBootServletInitializer() {
    var appLogger = LoggerFactory.getLogger(ParktrisServerApplication::class.java)

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(ParktrisServerApplication::class.java!!)
    }

    @Value("\${parktris.cors.allowedOrigins}")
    var allowedOrigins: String=""

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurerAdapter() {
            override fun addCorsMappings(registry: CorsRegistry?) {
                appLogger.info("******************adding CORS mapping with allowed origins="+allowedOrigins)
                registry!!.addMapping("/**").allowedOrigins(allowedOrigins).allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
            }
        }
    }

    @Bean
    fun getFreeSlotDeclarationLogic(): FreeSlotDeclarationLogic{
        return FreeSlotDeclarationLogic()
    }
    @Bean
    fun getLoanLogic(): LoanLogic{
        return LoanLogic()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(ParktrisServerApplication::class.java, *args)
}
