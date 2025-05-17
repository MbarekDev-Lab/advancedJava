package implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private Map<ID, T> store = new HashMap<>();

    public T findById(ID id) {
        return store.get(id);
    }

    public void save(T entity) {
        // requires entity to expose ID, this is simplified
        System.out.println("Saving entity: " + entity);
    }

    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }
}