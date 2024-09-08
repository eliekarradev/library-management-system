package libraryMS.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Around("execution(* libraryMS.controller.*.*(..)) || execution(* libraryMS.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        logger.info(joinPoint.getSignature() + " executed in " + executionTime + " ms");
        return proceed;
    }

    @AfterThrowing(pointcut = "execution(* libraryMS.controller.*.*(..)) || execution(* libraryMS.service.*.*(..))", throwing = "exception")
    public void logExceptions(Exception exception) {
        logger.error("An exception occurred: " + exception.getMessage());
    }

}
