package api;

import model.Doctor;
import util.DatabaseUtil;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class DoctorAPI {

    public DoctorAPI() {
        DatabaseUtil.setDb(DatabaseUtil.getInstance());
    }

    public Optional<Doctor> get(int id) {
        return Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(Doctor.class, id));
    }

    public List<Doctor> getAll() {
        TypedQuery<Doctor> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Doctor u", Doctor.class);
        return query.getResultList();
    }

    public void create(Doctor doctor) {
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.persist(doctor));
    }

    public void update(Doctor oldDoctor, Doctor newDoctor) {
        oldDoctor.setFirstName(newDoctor.getFirstName());
        oldDoctor.setLastName(newDoctor.getLastName());
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(oldDoctor));
    }

    public void delete(Doctor doctor) {
        doctor.setIsDeleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(doctor));
    }
}
