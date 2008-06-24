package codalyze.ui.web.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import codalyze.core.entity.SQLQuery;
import codalyze.ui.web.controllers.SQLQueryController;

public class CreateQueryValidator implements Validator {

	@Override
	public boolean supports(Class clazz) {
		return SQLQuery.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "query", "please.insert.query", "please.insert.query");
		ValidationUtils.rejectIfEmpty(errors, "title", "please.insert.title", "please.insert.title");
	}
}
