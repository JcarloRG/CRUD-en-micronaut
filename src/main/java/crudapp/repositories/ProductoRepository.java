package crudapp.repositories;

import crudapp.models.Producto;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

    @Override
    @Join("categoria") 
    List<Producto> findAll();

    @Override
    @Join("categoria") 
    Optional<Producto> findById(Long id);
}
