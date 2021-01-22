package vip.gadfly.chakkispring.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StudentClassCheck {
    String valueName() default "";

    String valueType() default "";

    String paramType() default "";
}

