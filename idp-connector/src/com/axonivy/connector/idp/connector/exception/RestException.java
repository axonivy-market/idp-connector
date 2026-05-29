package com.axonivy.connector.idp.connector.exception;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

public class RestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int statusCode;
	private String operation;
	private String errorMessage;

	public RestException(Response response, String operation, String errorMessage) {
		this.operation = operation;
		if (response != null) {
			this.statusCode = response.getStatus();
			if (StringUtils.isNotBlank(errorMessage)) {
				this.errorMessage = errorMessage;
			} else {
				this.errorMessage = "Calling IDP API Failed";
			}
		}

	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "RestException{\n" + "operation= " + operation + ",\n statusCode= " + statusCode
				+ ", \\n errorMessage= '" + errorMessage + '\'' + "\n }";
	}
}