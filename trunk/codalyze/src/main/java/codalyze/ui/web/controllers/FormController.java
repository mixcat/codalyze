package codalyze.ui.web.controllers;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import codalyze.core.entity.SQLQuery;

import com.sun.ws.rest.impl.ResponseBuilderImpl;

public class FormController {

	private Class<SQLQuery> commandClass;
	protected Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	protected final Errors bindAndValidate(Object command, MultivaluedMap<String, String> form) {
		PropertyValues pvs = getPropertyValues(form);
		Errors errors = getBindingResult(command, pvs);
		if (validator != null) {
			validator.validate(command, errors);
		}
		if (errors.hasErrors()) {
			throw getValidationException(errors.getAllErrors());
		}
		return errors;
	}

	protected RuntimeException getValidationException(List<?> allErrors) {
		return new WebApplicationException(
				new ResponseBuilderImpl()
				.status(500)
				.entity(JSONArray.fromObject(allErrors).toString())
				.type("application/javascript")
				.build()
			);
	}
	
	protected RuntimeException getNotFoundException(String path) {
		return new WebApplicationException(
				new ResponseBuilderImpl()
				.status(404)
				.entity("{'not.found':'" + path +"'}")
				.type("application/javascript")
				.build()
			);
	}
	
	protected void setCommandClass(Class<SQLQuery> commandClass) {
		this.commandClass = commandClass;
	}
	
	protected PropertyValues getPropertyValues(MultivaluedMap<String, String> form) {
		MutablePropertyValues pvs = new MutablePropertyValues();
		for (String key : form.keySet()) {
			pvs.addPropertyValue(key, form.getFirst(key));
		}
		return pvs;
	}

	protected Errors getBindingResult(Object command, PropertyValues pvs) {
		DataBinder binder = new DataBinder(command);
		binder.bind(pvs);
		return binder.getBindingResult();
	}

	protected Object formBackingObject() {
		try {
			return commandClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}