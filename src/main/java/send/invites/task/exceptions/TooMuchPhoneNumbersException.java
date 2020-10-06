package send.invites.task.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PAYMENT_REQUIRED, reason = "BAD_REQUEST")
public class TooMuchPhoneNumbersException extends Exception {

	private static final long serialVersionUID = -7766767000245897356L;

	public TooMuchPhoneNumbersException() {
		super();
	}

	
}
