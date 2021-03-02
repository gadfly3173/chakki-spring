package vip.gadfly.chakkispring.common.annotation;

import vip.gadfly.chakkispring.common.validator.MultimediaWordCountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = MultimediaWordCountValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultimediaWordCount {

    String message() default "{org.hibernate.validator.constraints.Length.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    int min() default 0;

    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;
}
