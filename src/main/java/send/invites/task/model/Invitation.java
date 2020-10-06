package send.invites.task.model;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@NamedStoredProcedureQueries(
		value = {
				@NamedStoredProcedureQuery(
						name = "invite",
						procedureName = "invite",
						parameters = {
								@StoredProcedureParameter(name = "user_id", type = Long.class, mode = ParameterMode.IN),
								@StoredProcedureParameter(name = "phones", type = List.class, mode = ParameterMode.IN)
						}
				),
				@NamedStoredProcedureQuery(
						name = "getCountInvitations",
						procedureName = "getCountInvitations",
						parameters = {
								@StoredProcedureParameter(name = "app_id", type = Long.class, mode = ParameterMode.IN)
						},
						resultClasses = Integer.class
				)		
		}
	)
@Entity
@Data
@Table(name= "invitation")
public class Invitation {
		
	/**
	 * invitation id 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	/**
	 * invitation author
	 */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author")
	private User author;
	
	/**
	 * phone number from contact list
	 */
	@Column(name = "phone")
	private String phone;
	
	/**
	 * invitation creation date
	 */
	@Column(name = "create_done")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createDone;
	
	
}
