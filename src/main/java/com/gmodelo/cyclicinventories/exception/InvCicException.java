package com.gmodelo.cyclicinventories.exception;

import com.gmodelo.cyclicinventories.beans.E_Error_SapEntity;

public class InvCicException extends Exception {

	private E_Error_SapEntity sapError;

	public E_Error_SapEntity getSapError() {
		return sapError;
	}

	public void setSapError(E_Error_SapEntity sapError) {
		this.sapError = sapError;
	}

	private static final long serialVersionUID = 1L;

	public InvCicException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvCicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvCicException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvCicException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvCicException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvCicException(Throwable cause, E_Error_SapEntity sapError) {
		super(cause);
		this.sapError = sapError;
	}

}
