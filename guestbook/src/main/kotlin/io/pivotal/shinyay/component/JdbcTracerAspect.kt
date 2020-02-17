package io.pivotal.shinyay.component

import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import brave.Tracer

@Aspect
@Component
class JdbcTracerAspect(val tracer: Tracer) {
    
}