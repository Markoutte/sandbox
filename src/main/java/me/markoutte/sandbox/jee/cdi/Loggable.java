package me.markoutte.sandbox.jee.cdi;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RUNTIME)
public @interface Loggable {
}
