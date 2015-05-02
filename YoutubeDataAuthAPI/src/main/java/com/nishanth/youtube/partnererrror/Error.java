package com.nishanth.youtube.partnererrror;

import java.util.List;

public class Error {
	
	private String error;
	private String code;
	private String message;
	private List<Errors> errors;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Errors> getErrors() {
		return errors;
	}
	public void setErrors(List<Errors> errors) {
		this.errors = errors;
	}
	
}
