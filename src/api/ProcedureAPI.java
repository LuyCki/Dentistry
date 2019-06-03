package api;

import model.Procedure;
import util.DatabaseUtil;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ProcedureAPI {

    public ProcedureAPI() {
        DatabaseUtil.setDb(DatabaseUtil.getInstance());
    }

    public Optional<Procedure> get(int id) {
        return Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(Procedure.class, id));
    }

    public List<Procedure> getAll() {
        TypedQuery<Procedure> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM Procedure u", Procedure.class);
        return query.getResultList();
    }

    public void create(Procedure procedure) {
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.persist(procedure));
    }

    public void update(Procedure oldProcedure, Procedure newProcedure) {
        oldProcedure.setName(newProcedure.getName());
        oldProcedure.setPrice(newProcedure.getPrice());
        oldProcedure.setStartDate(newProcedure.getStartDate());
        oldProcedure.setEndDate(newProcedure.getEndDate());
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(oldProcedure));
    }

    public void delete(Procedure procedure) {
        procedure.setIsDeleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(procedure));
    }

    public void updatePrice (Procedure oldProcedure, Procedure newPriceProcedure){

    }
}
