package com.example.its.domain.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect    // Declares this class as an Aspect
@Component // Makes it a Spring-managed bean
public class LoggingAspect {

	// Defines the pointcut and the advice
	@Around("@annotation(LogExecuteInfo)")
	public Object logExecuteInfo(ProceedingJoinPoint joinPoint, LogExecuteInfo logger) throws Throwable {
		final long started = System.currentTimeMillis();
		
		Object result = joinPoint.proceed();
		
		final long ended = System.currentTimeMillis();
		//System.out.printf("INFO: %s() was executed in %d %s.\n", joinPoint.getSignature().getName(), (ended - started), logger.unit());
		
		return result;
	}
}
