package ru.zhem.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.zhem.common.annotation.LoggingAnnotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(ru.zhem.common.annotation.LoggingAnnotation)")
    public void logPointCut(){
    }

    @Around(value = "logPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long time = System.currentTimeMillis() - startTime;
        doLog(joinPoint, time);
        return result;
    }

    private void doLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LoggingAnnotation loggingAnnotation = method.getAnnotation(LoggingAnnotation.class);
        String module = loggingAnnotation.module();
        String operation = loggingAnnotation.operation();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        List<String> methodArgs = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .toList();
        String methodArgsToString = String.join(", ",methodArgs);
        String methodFullName = className + "." + methodName + "(" + methodArgsToString + ")";

        log.debug("In module {} completed method {} for operation '{}' in {} ms",
                module, methodFullName, operation, time);
    }

}
