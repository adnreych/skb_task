package send.invites.task.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PhoneNumberFormatException extends Exception {
	
	private static final long serialVersionUID = -4110130118416951107L;

	public PhoneNumberFormatException() {
        super();
    }

}
