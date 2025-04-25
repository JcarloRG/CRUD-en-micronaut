package crudapp.repositories;

import crudapp.models.Categoria;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}
