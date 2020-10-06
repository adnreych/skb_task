package send.invites.task.controller;

import javax.annotation.Priority;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Priority(value = 2)
public class DefaultControllerAdvice  {
	
	 @ExceptionHandler(value = Exception.class)
	 public ResponseEntity<String> exception(Exception exception, WebRequest request) {
		 return new ResponseEntity<String>("500 INTERNAL SMS_SERVICE:" + exception.getMessage() + exception.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	 }
}
