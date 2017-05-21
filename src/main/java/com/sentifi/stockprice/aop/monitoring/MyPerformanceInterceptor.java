package com.sentifi.stockprice.aop.monitoring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyPerformanceInterceptor implements MethodInterceptor {

   Logger logger = LoggerFactory.getLogger(MyPerformanceInterceptor.class.getName());
   private static long methodWarningThreshold = 1000;

   public Object invoke(MethodInvocation method) throws Throwable {
       long start = System.currentTimeMillis();
       try {
           return method.proceed();
       }
       finally {
           updateStats(method.getMethod().getDeclaringClass().getSimpleName() + "." + method.getMethod().getName(),(System.currentTimeMillis() - start));
       }
   }

   private void updateStats(String methodName, long elapsedTime) {
       if(elapsedTime > methodWarningThreshold) {
           logger.warn("This method run for a long time: " + methodName + "()" + "--------Time taken: " +elapsedTime);
       }
      
   }
}