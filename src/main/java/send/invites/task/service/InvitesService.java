package send.invites.task.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.Transliterator;
import send.invites.task.exceptions.DublicateNumbersException;
import send.invites.task.exceptions.EmptyMessageException;
import send.invites.task.exceptions.InvalidMessageException;
import send.invites.task.exceptions.PhoneNumberEmptyException;
import send.invites.task.exceptions.PhoneNumberFormatException;
import send.invites.task.exceptions.TooLongMessageException;
import send.invites.task.exceptions.TooMuchInvitesException;
import send.invites.task.exceptions.TooMuchPhoneNumbersException;
import send.invites.task.model.InvitationResourse;
import send.invites.task.repository.InvitationRepository;

@Service
public class InvitesService {
	
	@Autowired
	public InvitesService(InvitationRepository invitationRepository) {
		this.invitationRepository = invitationRepository;
	}
	
	@PersistenceContext
	private EntityManager manager;
	
	private final InvitationRepository invitationRepository;
	
	public List<String> sendInvites(InvitationResourse invitationResourse) throws PhoneNumberFormatException, PhoneNumberEmptyException, 
		TooMuchPhoneNumbersException, DublicateNumbersException, EmptyMessageException, InvalidMessageException, TooLongMessageException, 
		TooMuchInvitesException {
		
		Transliterator toLatinTrans = Transliterator.getInstance("Cyrillic-Latin");
		boolean fullLatinString = true;
		
		List<String> phoneNumbers = invitationResourse.getPhoneNumbers();
		String message = invitationResourse.getMessage();
		
		if (phoneNumbers.isEmpty()) {
			throw new PhoneNumberEmptyException();
		}
		
		if (phoneNumbers.size() > 16) {
			throw new TooMuchPhoneNumbersException();
		}
		
		if (!phoneNumbers.stream().allMatch(e -> e.matches("^7[0-9]{10}$"))) {
			throw new PhoneNumberFormatException();
		}
		
		if (new HashSet<String>(phoneNumbers).size() < phoneNumbers.size()) {
			throw new DublicateNumbersException();		
		}
		
		if (message == "") {
			throw new EmptyMessageException();
		}
		
		String result = "";
		for (int i = 0; i < message.length(); i++) {
		    if ((message.charAt(i) >= 128 && message.charAt(i) <= 175) || (message.charAt(i) >= 224 && message.charAt(i) <= 241)) {
		    	String translitChar = toLatinTrans.transliterate(String.valueOf(message.charAt(i)));
		    	result = result + translitChar;
		    	fullLatinString = false;
		    } else if (message.charAt(i) >= 0 && message.charAt(i) <= 127) {
		    	result = result + String.valueOf(message.charAt(i));
		    } else {
		    	throw new InvalidMessageException();
		    }
		}
		
		message = result;
		
		if (fullLatinString) {
			if (message.length() > 160) 
				throw new TooLongMessageException();
		} else {
			if (message.length() > 128) 
				throw new TooLongMessageException();
		}
		
		StoredProcedureQuery storedProcedureCountInvitations = manager.createNamedStoredProcedureQuery("getCountInvitations")
				.registerStoredProcedureParameter(1, Long.class, ParameterMode.IN);

		storedProcedureCountInvitations.registerStoredProcedureParameter(2 , Integer.class , ParameterMode.OUT);			
		storedProcedureCountInvitations.setParameter(1, 4); // app_id == 4	
		storedProcedureCountInvitations.execute();
		
		Integer countInvitations = (Integer)storedProcedureCountInvitations.getOutputParameterValue(0);
		
		if ((countInvitations + phoneNumbers.size()) > 128) {
			throw new TooMuchInvitesException();
		}
		
		List<String> phoneNumbersToDelete = phoneNumbers  // remove already invites phones
				.stream()
				.filter(phone -> invitationRepository.findAllByPhone(phone).size() != 0)
				.collect(Collectors.toList());
		
		phoneNumbers.removeAll(phoneNumbersToDelete);		
		
		StoredProcedureQuery storedProcedureInviteSend = manager.createNamedStoredProcedureQuery("invite")
				.registerStoredProcedureParameter(0 , Long.class , ParameterMode.IN)
				.registerStoredProcedureParameter(1 , List.class , ParameterMode.IN)
				.setParameter(0, 7)	// user_id == 7
				.setParameter(1, phoneNumbers); 
		
		storedProcedureInviteSend.execute();
		
		return phoneNumbers;		
	}
	
	

}
