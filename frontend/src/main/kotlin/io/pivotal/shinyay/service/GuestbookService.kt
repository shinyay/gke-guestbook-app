package io.pivotal.shinyay.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Service
class GuestbookService(val restTemplate: RestTemplate, val endPoint: String) {

    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    fun add(username: String, message: String): Map<String, String> {
        return restTemplate.postForObject(endPoint, mapOf("username" to username, "message" to message, "timestamp" to dateFormat.format(Date())))
    }
}