package io.pivotal.shinyay.repository

import io.pivotal.shinyay.entity.Message
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface MessageRepository : PagingAndSortingRepository<Message, Long>