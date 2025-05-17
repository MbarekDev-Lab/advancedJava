package implementations;

import java.util.List;

interface Repository<T, ID> {
    T findById(ID id);
    void save(T entity);
    List<T> findAll();
}