package api;

import model.Patient;
import util.DatabaseUtil;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class PatientAPI {

    public PatientAPI() {
        DatabaseUtil.setDb(DatabaseUtil.getInstance());
    }

    public Optional<Patient> get(int id) {
        return Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(Patient.class, id));
    }

    public List<Patient> getAll() {
        TypedQuery<Patient> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Patient u", Patient.class);
        return query.getResultList();
    }

    public void create(Patient patient) {
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.persist(patient));
    }

    public void update(Patient oldPatient, Patient newPatient) {
        oldPatient.setFirstName(newPatient.getFirstName());
        oldPatient.setLastName(newPatient.getLastName());
        oldPatient.setAge(newPatient.getAge());
        oldPatient.setPhone(newPatient.getPhone());
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(oldPatient));
    }

    public void delete(Patient patient) {
        patient.setIsDeleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(patient));
    }

    public List<Patient> getAllPatientsNotDeleted() {
        TypedQuery<Patient> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Patient u WHERE u.isDeleted = 0", Patient.class);
        return query.getResultList();
    }
}
