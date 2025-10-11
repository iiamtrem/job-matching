package com.jobmatching.userservice.common;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface AuditLog {
    String action();
}
