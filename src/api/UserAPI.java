package api;

import model.User;
import util.DatabaseUtil;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class UserAPI {
    public UserAPI() {
        DatabaseUtil.setDb(DatabaseUtil.getInstance());
    }

    public Optional<User> get(int id) {
        return Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(User.class, id));
    }

    public List<User> getAll() {
        TypedQuery<User> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public void create(User user) {
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.persist(user));
    }

    public void update(User oldUser, User newUser) {
        oldUser.setUsername(newUser.getUsername());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setRole(newUser.getRole());
        oldUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(oldUser));
    }

    public void delete(User user) {
        user.setIsDeleted((byte) 1);
        DatabaseUtil.getDb().executeTransaction(entityManager -> entityManager.merge(user));
    }

    public User requestLogin(String username, String password) {

        TypedQuery<User> query = DatabaseUtil.getDb().getEntityManager().createQuery("SELECT u FROM User u " +
                "WHERE u.username = :name AND u.password = :pass", User.class);
        query.setParameter("name", username);
        query.setParameter("pass", password);

        try {
            User request = query.getSingleResult();
            return request;
        } catch (NoResultException e) {
            return null;
        }
    }
}
