package gr.europeandynamics.web.technico.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {

    Optional<T> save(T t);

    Optional<T> getById(K id);

    List<T> getAll();

    boolean deleteById(K id);
}
