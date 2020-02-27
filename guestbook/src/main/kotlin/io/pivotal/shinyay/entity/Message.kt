package io.pivotal.shinyay.entity

import com.fasterxml.jackson.annotation.JsonInclude
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Message(
        @Id
//        @GeneratedValue(generator = "uuid2")
//        @GenericGenerator(name = "uuid2", strategy = "uuid2")
//        @Column(columnDefinition = "varchar(36)")
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long ,
        val username: String,
        val message: String
)