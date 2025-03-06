package com.beiji.transactionmanager.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 切面实现逻辑
 */
@Aspect
@Component
public class SecurityLogAspect {
    // 定义切入点：匹配com.example包下所有服务类中的所有方法
    @Around("@annotation(securityLog)")
    public Object doAction(ProceedingJoinPoint joinPoint, SecurityLog securityLog) throws Throwable {
        long start = System.currentTimeMillis();
        // 执行目标方法
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(joinPoint.getSignature().getName()+ " executed. SecurityLevel: " + securityLog.level() + ". TimeCost: in " + executionTime + "ms");
        return proceed;
    }
}
