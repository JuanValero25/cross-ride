package com.crossover.techtrial.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.crossover.techtrial.exceptions.ModelValidationException;

@Component
public class ModelValidator implements Validator {

	@Autowired
	private LocalValidatorFactoryBean validator;

	@Override
	public void validate(Object model, Errors errors) {
		validator.validate(model, errors);
		if (errors.hasErrors()) {
			throw new ModelValidationException("Server validator found error(s)", errors);
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
