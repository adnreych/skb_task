package send.invites.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import send.invites.task.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
	
	List<Invitation> findAllByPhone(String phone);
}
