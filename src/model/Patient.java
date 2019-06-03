package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="patients")
@NamedQuery(name="Patient.findAll", query="SELECT p FROM Patient p")
public class Patient extends RecursiveTreeObject<Patient> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_patient", unique=true, nullable=false)
	private int idPatient;

	@Column(nullable=false)
	private int age;

	@Column(name="first_name", nullable=false, length=45)
	private String firstName;

	private byte isDeleted;

	@Column(name="last_name", nullable=false, length=45)
	private String lastName;

	@Column(nullable=false)
	private int phone;

	//bi-directional many-to-one association to Appointment
	@OneToMany(mappedBy="patient")
	private List<Appointment> appointments;

	public Patient() {
	}

	public int getIdPatient() {
		return this.idPatient;
	}

	public void setIdPatient(int idPatient) {
		this.idPatient = idPatient;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public int getPhone() {
		return this.phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Appointment addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
		appointment.setPatient(this);

		return appointment;
	}

	public Appointment removeAppointment(Appointment appointment) {
		getAppointments().remove(appointment);
		appointment.setPatient(null);

		return appointment;
	}

}