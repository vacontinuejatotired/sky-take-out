package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void AutoFillAspect() {}

    @Before("AutoFillAspect()")
    public void before(JoinPoint joinPoint) {
        log.info("AutoFillAspect start");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//拿到方法签名
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//得到注解
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args[0] == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();
        Object entity = args[0];
        if( operationType == OperationType.INSERT){
            try {
                Method setCreateTime= entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime= entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod("setCreateUser",Long.class);
                Method setUpdateUser= entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);//注意long的class文件开头要大写
                setUpdateTime.invoke(entity,now);
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,id);
                setUpdateUser.invoke(entity,id);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {

            try {
                Method setUpdateTime= entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
                Method setUpdateUser= entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);//注意long的class文件开头要大写
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,id);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
