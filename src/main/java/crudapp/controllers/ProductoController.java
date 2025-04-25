package crudapp.controllers;

import crudapp.models.Producto;
import crudapp.models.Categoria;
import crudapp.repositories.ProductoRepository;
import crudapp.repositories.CategoriaRepository;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;

import java.net.URI;
import java.util.*;

@Controller("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Get("/")
    @View("productos")
    public Map<String, Object> listar() {
        Map<String, Object> model = new HashMap<>();
        model.put("productos", productoRepository.findAll());
        return model;
    }

    @Get("/nuevo")
    @View("nuevo_producto")
    public Map<String, Object> nuevoForm() {
        Map<String, Object> model = new HashMap<>();
        model.put("categorias", categoriaRepository.findAll());
        return model;
    }

    @Post(value = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> crear(@Body Producto producto, @Body("categoria.id") Long categoriaId) {
        categoriaRepository.findById(categoriaId).ifPresent(producto::setCategoria);
        productoRepository.save(producto);
        return HttpResponse.redirect(URI.create("/productos"));
    }

    @Get("/editar/{id}")
    @View("nuevo_producto")
    public Map<String, Object> editarVista(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            model.put("producto", producto.get());
            model.put("categorias", categoriaRepository.findAll());
        }
        return model;
    }

    @Post(value = "/editar", consumes = MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> editar(@Body Producto producto, @Body("categoria.id") Long categoriaId) {
        categoriaRepository.findById(categoriaId).ifPresent(producto::setCategoria);
        productoRepository.update(producto);
        return HttpResponse.redirect(URI.create("/productos"));
    }

    @Get("/eliminar/{id}")
    public HttpResponse<?> eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return HttpResponse.redirect(URI.create("/productos"));
    }
}
