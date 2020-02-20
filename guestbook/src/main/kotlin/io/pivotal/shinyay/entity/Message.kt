package io.pivotal.shinyay.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Message(
        @Id
        @GeneratedValue
        val id: Long,
        val username: String,
        val message: String
)