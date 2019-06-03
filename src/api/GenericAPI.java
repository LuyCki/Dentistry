package api;

import java.util.List;
import java.util.Optional;

public interface GenericAPI<TYPE> {
    //Generic methods for CRUD
    Optional<TYPE> get(int id);
    List<TYPE> getAll();
    void create(TYPE type);
    void update(TYPE old, TYPE newObj);
    void delete(TYPE type);
}
