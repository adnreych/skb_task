package send.invites.task.model;

import java.util.List;

import lombok.Data;

@Data
public class InvitationResourse {

	List<String> phoneNumbers;
	String message;
}
