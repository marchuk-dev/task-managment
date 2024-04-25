package com.miratech.miratechtechtask.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    List<String> valueList = new ArrayList<>();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(String.join("", value.toUpperCase().split(" ")));
    }

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            valueList.add(String.join("", enumVal.toString().toUpperCase().split("_")));
        }
    }

}