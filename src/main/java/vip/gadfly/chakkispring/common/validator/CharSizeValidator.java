package vip.gadfly.chakkispring.common.validator;

import vip.gadfly.chakkispring.common.annotation.CharSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CharSizeValidator implements ConstraintValidator<CharSize, String> {

    public int maxCharSize;
    public int minCharSize;

    public void initialize(CharSize charSize) {
        this.maxCharSize = charSize.max();
        this.minCharSize = charSize.min();
    }

    @Override
    public boolean isValid(String StringValue, ConstraintValidatorContext context) {

        if (StringValue == null) {
            return true;
        }
        int f = StringValue.getBytes().length;
        return f >= minCharSize && f <= maxCharSize;
    }
}
