package org.soframel.parktris.parktrisserver.security

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.io.IOException
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

/**
 * used as a JSon deserializer to encrypt password as soon as it is deserialzed
 */
class BCryptPasswordDeserializer : JsonDeserializer<String>() {

    @Throws(IOException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): String {
        val oc = jsonParser.getCodec()
        val node: JsonNode = oc.readTree(jsonParser)
        val encoder = BCryptPasswordEncoder(11)
        return encoder.encode(node.asText())
    }
}