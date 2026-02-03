package repository;

import java.util.List;

public interface CrudRepository<T> {

    T save(T entity);

    T findById(int id);

    List<T> findAll();

    void deleteById(int id);
}
