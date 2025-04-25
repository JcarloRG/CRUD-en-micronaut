package crudapp.controllers;

import crudapp.models.Categoria;
import crudapp.repositories.CategoriaRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/")
public class HomeController {

    private final CategoriaRepository categoriaRepository;

    public HomeController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Get
    @View("categorias") // ← sin extensión .html
    public Map<String, Object> index() {
        List<Categoria> categorias = (List<Categoria>) categoriaRepository.findAll();
        Map<String, Object> model = new HashMap<>();
        model.put("categorias", categorias);
        return model;
    }
}
