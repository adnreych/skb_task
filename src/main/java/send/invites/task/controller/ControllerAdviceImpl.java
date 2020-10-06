package send.invites.task.controller;

import javax.annotation.Priority;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import send.invites.task.exceptions.DublicateNumbersException;
import send.invites.task.exceptions.EmptyMessageException;
import send.invites.task.exceptions.InvalidMessageException;
import send.invites.task.exceptions.PhoneNumberEmptyException;
import send.invites.task.exceptions.PhoneNumberFormatException;
import send.invites.task.exceptions.TooLongMessageException;
import send.invites.task.exceptions.TooMuchInvitesException;
import send.invites.task.exceptions.TooMuchPhoneNumbersException;

@ControllerAdvice
@Priority(value = 1)
public class ControllerAdviceImpl {

	@ExceptionHandler(TooLongMessageException.class)
    protected ResponseEntity<SpecificException> handleTooLongMessageException() {
        return new ResponseEntity<>(new SpecificException(407, "BAD_REQUEST MESSAGE_INVALID: Invite message too long, should be less or equal to 128 characters"),
        		HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
    }
	
	@ExceptionHandler(DublicateNumbersException.class)
    protected ResponseEntity<SpecificException> handleDublicateNumbersException() {
        return new ResponseEntity<>(new SpecificException(404, "BAD_REQUEST PHONE_NUMBERS_INVALID: Dublicate numbers detected"),
        		HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(EmptyMessageException.class)
    protected ResponseEntity<SpecificException> handleEmptyMessageException() {
        return new ResponseEntity<>(new SpecificException(405, "BAD_REQUEST MESSAGE_EMPTY: Invite message is missing"),
        		HttpStatus.METHOD_NOT_ALLOWED);
    }
	
	@ExceptionHandler(InvalidMessageException.class)
    protected ResponseEntity<SpecificException> handleInvalidMessageException() {
        return new ResponseEntity<>(new SpecificException(406, "BAD_REQUEST MESSAGE_INVALID: Invite message should be contain only characters in 7 bit GSM encoding"),
        		HttpStatus.NOT_ACCEPTABLE);
    }
	
	@ExceptionHandler(PhoneNumberEmptyException.class)
    protected ResponseEntity<SpecificException> handlePhoneNumberEmptyException() {
        return new ResponseEntity<>(new SpecificException(401, "BAD_REQUEST PHONE_NUMBERS_EMPTY: Phone number is missing"),
        		HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(PhoneNumberFormatException.class)
    protected ResponseEntity<SpecificException> handlePhoneNumberFormatException() {
        return new ResponseEntity<>(new SpecificException(400, "BAD_REQUEST PHONE_NUMBERS_INVALID: One or several phone numbers do not match with international format"),
        		HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(TooMuchInvitesException.class)
    protected ResponseEntity<SpecificException> handleTooMuchInvitesException() {
        return new ResponseEntity<>(new SpecificException(403, "BAD_REQUEST PHONE_NUMBERS_INVALID: Too much phone numbers, should be less or equal to 128 per day"),
        		HttpStatus.FORBIDDEN);
    }
	
	@ExceptionHandler(TooMuchPhoneNumbersException.class)
    protected ResponseEntity<SpecificException> handleTooMuchPhoneNumbersException() {
        return new ResponseEntity<>(new SpecificException(402, "BAD_REQUEST PHONE_NUMBERS_INVALID: Too much phone numbers, should be less or equal to 16 per request"),
        		HttpStatus.PAYMENT_REQUIRED);
    }

    @Data
    @AllArgsConstructor
    private static class SpecificException {
    	private int status;
        private String error;
    }
}
