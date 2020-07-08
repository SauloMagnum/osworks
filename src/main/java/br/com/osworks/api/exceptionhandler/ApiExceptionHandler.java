package br.com.osworks.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.osworks.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;

		/*var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());*/
		
		return handleExceptionInternal(ex, getProblema(status, ex.getMessage()), new HttpHeaders(), status, request);
		//return handleExceptionInternal(ex, new Problema(status.value(), LocalDateTime.now(), ex.getMessage(), new ArrayList<Problema.Campo>()), new HttpHeaders(), status, request);

	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> hadleEntidadeNaoEncontrada(NegocioException ex, WebRequest request) {
		//var status = HttpStatus.NOT_FOUND;

		/*var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(ex.getMessage());
		problema.setDataHora(OffsetDateTime.now());*/
		
		return handleExceptionInternal(ex, getProblema(HttpStatus.NOT_FOUND, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		//return handleExceptionInternal(ex, new Problema(status.value(), LocalDateTime.now(), ex.getMessage(), new ArrayList<Problema.Campo>()), new HttpHeaders(), status, request);

	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var campos = new ArrayList<Problema.Campo>();
		var problema = new Problema();

		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nomeCampo = ((FieldError) error).getField();
			String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());

			campos.add(new Problema.Campo(nomeCampo, message));
		}

		problema.setStatus(status.value());
		problema.setTitulo("Um ou mais campos estao invalidos");
		problema.setDataHora(OffsetDateTime.now());
		problema.setCampos(campos);

		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}
	
	private Problema getProblema(HttpStatus status, String message) {
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setTitulo(message);
		problema.setDataHora(OffsetDateTime.now());
		
		return problema;
	}
}
