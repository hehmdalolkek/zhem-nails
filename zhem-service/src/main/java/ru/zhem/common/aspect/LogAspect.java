package ru.zhem.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.zhem.common.annotation.LogAnnotation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(ru.zhem.common.annotation.LogAnnotation)")
    public void logPointCut() {
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
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        String module = logAnnotation.module();
        String operation = logAnnotation.operation();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        List<String> methodArgs = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .toList();
        String methodArgsToString = String.join(", ", methodArgs);
        String methodFullName = className + "." + methodName + "(" + methodArgsToString + ")";

        log.debug("In module {} completed method {} for operation '{}' in {} ms",
                module, methodFullName, operation, time);
    }

}
