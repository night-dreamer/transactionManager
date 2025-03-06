package com.beiji.transactionmanager.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 记录安全操作日志，并统计执行时间
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecurityLog {
    String level() default "NORMAL"; // 默认级别为NORMAL
}
