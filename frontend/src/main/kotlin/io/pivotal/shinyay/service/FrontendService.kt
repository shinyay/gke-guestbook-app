package io.pivotal.shinyay.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class FrontendService(val restTemplate: RestTemplate, val endPoint: String) {
    fun greeting(name: String): Map<String, String> = restTemplate.getForObject("$endPoint/$name")
}