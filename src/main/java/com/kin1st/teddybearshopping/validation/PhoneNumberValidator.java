package com.kin1st.teddybearshopping.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(\\+84|0)[1-9][0-9]{8}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return PHONE_PATTERN.matcher(value).matches();
    }
}
