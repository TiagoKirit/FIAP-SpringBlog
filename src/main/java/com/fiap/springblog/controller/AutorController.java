package com.fiap.springblog.controller;

import com.fiap.springblog.model.Autor;
import com.fiap.springblog.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping("/criar")
    public Autor criar(@RequestBody Autor autor){
        return this.autorService.criar(autor);
    }

    @GetMapping("/{codigo}")
    public Autor obterPorCodigo(@PathVariable String codigo){
        return this.autorService.obterPorCodigo(codigo);
    }

    @GetMapping
    public List<Autor> obterTodos(){
        return this.autorService.obterTodos();
    }

}
