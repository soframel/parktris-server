package org.soframel.parktris.parktrisserver

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer




@SpringBootApplication
@EnableMongoRepositories
@Configuration
class ParktrisServerApplication: SpringBootServletInitializer() {
    var appLogger = Logger.getLogger(ParktrisServerApplication::class.java)

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

}

fun main(args: Array<String>) {
    SpringApplication.run(ParktrisServerApplication::class.java, *args)
}
