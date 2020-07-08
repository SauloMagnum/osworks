package br.com.osworks.domain.exception;

public class NegocioException extends RuntimeException{

	private static final long serialVersionUID = 8325865647379628553L;

	public NegocioException(String message) {
		super(message);
	}
}
