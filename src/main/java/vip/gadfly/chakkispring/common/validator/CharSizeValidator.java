package vip.gadfly.chakkispring.common.validator;

import vip.gadfly.chakkispring.common.annotation.CharSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CharSizeValidator implements ConstraintValidator<CharSize, String> {

    public int maxCharSize;

    public void initialize(CharSize charSize) {
        this.maxCharSize = charSize.max();
    }

    @Override
    public boolean isValid(String StringValue, ConstraintValidatorContext context) {

        if (StringValue == null) {
            return true;
        }
        int f = StringValue.getBytes().length;
        return f <= maxCharSize;
    }
}
