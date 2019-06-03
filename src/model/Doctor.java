package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="doctors")
@NamedQuery(name="Doctor.findAll", query="SELECT d FROM Doctor d")
public class Doctor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_doctor", unique=true, nullable=false)
	private int idDoctor;

	@Column(name="first_name", nullable=false, length=45)
	private String firstName;

	private byte isDeleted;

	@Column(name="last_name", nullable=false, length=45)
	private String lastName;

	//bi-directional many-to-one association to Appointment
	@OneToMany(mappedBy="doctor")
	private List<Appointment> appointments;

	//bi-directional one-to-one association to User
	@OneToOne
	@JoinColumn(name="id_doctor", nullable=false, insertable=false, updatable=false)
	private User user;

	public Doctor() {
	}

	public int getIdDoctor() {
		return this.idDoctor;
	}

	public void setIdDoctor(int idDoctor) {
		this.idDoctor = idDoctor;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public byte getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Appointment addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
		appointment.setDoctor(this);

		return appointment;
	}

	public Appointment removeAppointment(Appointment appointment) {
		getAppointments().remove(appointment);
		appointment.setDoctor(null);

		return appointment;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}