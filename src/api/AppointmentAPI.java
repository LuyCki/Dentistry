package api;

import model.Appointment;
import util.DatabaseUtil;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AppointmentAPI {

    public AppointmentAPI() {
        DatabaseUtil.setDb(DatabaseUtil.getInstance());
    }

    public Optional<Appointment> get(int id) {
        return Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(Appointment.class, id));
    }

    public List<Appointment> getAll() {
        TypedQuery<Appointment> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Appointment u", Appointment.class);
        return query.getResultList();
    }

    public void create(Appointment appointment) {
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.persist(appointment));
    }

    public void update(Appointment oldAppointment, Appointment newAppointment) {
        oldAppointment.setDoctor(newAppointment.getDoctor());
        oldAppointment.setPatient(newAppointment.getPatient());
        oldAppointment.setProcedure(newAppointment.getProcedure());
        oldAppointment.setDate(newAppointment.getDate());
        oldAppointment.setStartTime(newAppointment.getStartTime());
        oldAppointment.setEndTime(newAppointment.getEndTime());
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(oldAppointment));
    }

    public void delete(Appointment appointment) {
        appointment.setIsDeleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(appointment));
    }

    public void completed(Appointment appointment) {
        appointment.setIsCompleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(appointment));
    }

    public List<Appointment> getAllTodaysApptOrderByTime(int doctorId) {
        TypedQuery<Appointment> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Appointment u WHERE u.date = CURRENT_DATE AND u.doctor.idDoctor = :doctorId ORDER BY u.startTime", Appointment.class);
        query.setParameter("doctorId", doctorId);
        return query.getResultList();
    }

    public List<Appointment> getAllApptOrderByDateAndTime(int doctorId) {
        TypedQuery<Appointment> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Appointment u WHERE u.doctor.idDoctor = :doctorId ORDER BY u.date, u.startTime", Appointment.class);
        query.setParameter("doctorId", doctorId);
        return query.getResultList();
    }
}
