package io.pivotal.shinyay.component

import brave.Span
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import brave.Tracer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.springframework.cloud.sleuth.util.SpanNameUtil

@Aspect
@Component
class JdbcTracerAspect(val tracer: Tracer) {

    @Around("execution (* org.springframework.jdbc.core.JdbcTemplate.*(..))")
    fun traceJdbcCall(joinPoint: ProceedingJoinPoint): Any? {
        val spanName = SpanNameUtil.toLowerHyphen(joinPoint.signature.name)
        val span: Span = this.tracer.nextSpan().name("jdbc:/${spanName}").start()
        try {
            return joinPoint.proceed()
        } finally {
            span.finish()
        }
    }
}