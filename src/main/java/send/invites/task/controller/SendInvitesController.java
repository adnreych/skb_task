package send.invites.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import send.invites.task.exceptions.DublicateNumbersException;
import send.invites.task.exceptions.EmptyMessageException;
import send.invites.task.exceptions.InvalidMessageException;
import send.invites.task.exceptions.PhoneNumberEmptyException;
import send.invites.task.exceptions.PhoneNumberFormatException;
import send.invites.task.exceptions.TooLongMessageException;
import send.invites.task.exceptions.TooMuchInvitesException;
import send.invites.task.exceptions.TooMuchPhoneNumbersException;
import send.invites.task.model.InvitationResourse;
import send.invites.task.service.InvitesService;

@CrossOrigin
@RestController
public class SendInvitesController {

	@Autowired
	InvitesService invitesService;
	
	@PostMapping("/api/sendinvites")
	  public List<String> sendInvite(@RequestBody InvitationResourse invitationResourse) throws PhoneNumberFormatException, PhoneNumberEmptyException, 
	  TooMuchPhoneNumbersException, DublicateNumbersException, EmptyMessageException, InvalidMessageException, TooLongMessageException, TooMuchInvitesException {			
		return invitesService.sendInvites(invitationResourse);
	  }
}
