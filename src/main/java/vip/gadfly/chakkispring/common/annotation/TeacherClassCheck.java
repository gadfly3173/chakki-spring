package vip.gadfly.chakkispring.common.annotation;

import org.intellij.lang.annotations.MagicConstant;

import java.lang.annotation.*;

import static vip.gadfly.chakkispring.common.constant.ClassVerifyConstant.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TeacherClassCheck {
    String valueName() default "";

    @MagicConstant(stringValues = {classIdType, workIdType, signIdType, studentWorkIdType})
    String valueType() default "";

    @MagicConstant(stringValues = {requestBodyType, requestParamType, pathVariableType})
    String paramType() default "";
}

