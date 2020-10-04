package com.example.springadvanceapply.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

public class AopHelper {
    public  static Map<String, Object> getMethodParams(JoinPoint joinPoint){
        String[] names = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> params =new HashMap<String, Object>();
        if (names == null || names.length == 0) {
            return params;
        }
        Object[] values = joinPoint.getArgs();
        for (int i = 0; i < names.length; i++) {
            params.put(names[i], values[i]);
        }
        return params;
    }
}
