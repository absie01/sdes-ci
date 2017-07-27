package uk.gov.hmrc.sdes.event.service.api;

import javax.validation.ConstraintViolationException;

import org.springframework.amqp.AmqpException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Component
public class APIExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handle(final ConstraintViolationException e){  
        return e.getMessage();  
    }
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handle(final MissingServletRequestParameterException e){  
        return e.getMessage();  
    }
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handle(final MethodArgumentTypeMismatchException e){  
        return e.getMessage();  
    }
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST) 
    public String handle(final IllegalArgumentException e){  
        return e.getMessage();  
    }
	
	@ExceptionHandler(value = AmqpException.class) 
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
    public String handle(final AmqpException e){  
        return e.getMessage();  
    }
	
	@ExceptionHandler(value = Exception.class) 
	@ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR) 
    public String handle(final Exception e){  
        return e.getMessage();  
    }
}
