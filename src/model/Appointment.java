package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name="appointments")
@NamedQuery(name="Appointment.findAll", query="SELECT a FROM Appointment a")
public class Appointment extends RecursiveTreeObject<Appointment> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_appointment", unique=true, nullable=false)
	private int idAppointment;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date;

	@Column(name="end_time", nullable=false)
	private Time endTime;

	private byte isCompleted;

	private byte isDeleted;

	@Column(name="start_time", nullable=false)
	private Time startTime;

	//bi-directional many-to-one association to Doctor
	@ManyToOne
	@JoinColumn(name="id_doctor", nullable=false)
	private Doctor doctor;

	//bi-directional many-to-one association to Patient
	@ManyToOne
	@JoinColumn(name="id_patient", nullable=false)
	private Patient patient;

	//bi-directional many-to-one association to Procedure
	@ManyToOne
	@JoinColumn(name="id_procedure", nullable=false)
	private Procedure procedure;

	public Appointment() {
	}

	public int getIdAppointment() {
		return this.idAppointment;
	}

	public void setIdAppointment(int idAppointment) {
		this.idAppointment = idAppointment;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public byte getIsCompleted() {
		return this.isCompleted;
	}

	public void setIsCompleted(byte isCompleted) {
		this.isCompleted = isCompleted;
	}

	public byte getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Doctor getDoctor() {
		return this.doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Procedure getProcedure() {
		return this.procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

}