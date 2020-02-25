package io.pivotal.shinyay.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress

@RestController
class HelloController {
    @GetMapping("/hello/{name}")
    fun hello(@Value("\${greeting}") greetingTemplate: String,
              @Value("\${version}") version: String,
              @PathVariable name: String): Map<String, String> {
        val hostname = InetAddress.getLocalHost().hostName
        val greeting = greetingTemplate
                .replace("\$name", name)
                .replace("\$hostname", hostname)
                .replace("\$version", version)
        
        return mapOf<String, String>(
                "greeting" to greeting,
                "hostname" to hostname,
                "version" to version)
    }
}