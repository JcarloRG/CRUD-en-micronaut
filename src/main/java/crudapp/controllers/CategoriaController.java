package crudapp.controllers;

import crudapp.models.Categoria;
import crudapp.repositories.CategoriaRepository;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.views.View;

import java.net.URI; 
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller("/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Get("/")
    @View("categorias")
    public Map<String, Object> listar() {
        Map<String, Object> model = new HashMap<>();
        model.put("categorias", categoriaRepository.findAll());
        return model;
    }

    @Get("/nueva")
    @View("nueva_categoria")
    public void nuevaVista() {
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> crear(@Body Categoria categoria) {
        categoriaRepository.save(categoria);
        return HttpResponse.redirect(URI.create("/categorias"));
    }


    @Get("/editar/{id}")
    @View("nueva_categoria")
    public Map<String, Object> editarVista(@PathVariable Long id) {
        Map<String, Object> model = new HashMap<>();
        categoriaRepository.findById(id).ifPresent(c -> model.put("categoria", c));
        return model;
    }

    @Post("/editar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> editar(@Body Categoria categoria) {
        categoriaRepository.update(categoria);
        return HttpResponse.redirect(URI.create("/categorias"));
    }


    @Get("/eliminar/{id}")
    public HttpResponse<?> eliminar(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
        return HttpResponse.redirect(URI.create("/categorias"));
    }
}
