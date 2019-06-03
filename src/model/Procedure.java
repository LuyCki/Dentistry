package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="procedures")
@NamedQuery(name="Procedure.findAll", query="SELECT p FROM Procedure p")
public class Procedure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_procedure", unique=true, nullable=false)
	private int idProcedure;

	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date endDate;

	private byte isCompleted;

	private byte isDeleted;

	@Column(nullable=false, length=45)
	private String name;

	@Column(nullable=false)
	private int price;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date", nullable=false)
	private Date startDate;

	//bi-directional many-to-one association to Appointment
	@OneToMany(mappedBy="procedure")
	private List<Appointment> appointments;

	public Procedure() {
	}

	public int getIdProcedure() {
		return this.idProcedure;
	}

	public void setIdProcedure(int idProcedure) {
		this.idProcedure = idProcedure;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Appointment addAppointment(Appointment appointment) {
		getAppointments().add(appointment);
		appointment.setProcedure(this);

		return appointment;
	}

	public Appointment removeAppointment(Appointment appointment) {
		getAppointments().remove(appointment);
		appointment.setProcedure(null);

		return appointment;
	}

}