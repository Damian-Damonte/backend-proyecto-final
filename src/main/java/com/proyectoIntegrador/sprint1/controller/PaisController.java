package com.proyectoIntegrador.sprint1.controller;

import com.proyectoIntegrador.sprint1.model.Pais;
import com.proyectoIntegrador.sprint1.service.imp.PaisServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/paises")
public class PaisController {
    private final PaisServiceImp paisServiceImp;

    public PaisController(PaisServiceImp paisServiceImp) {
        this.paisServiceImp = paisServiceImp;
    }

    @GetMapping
    public ResponseEntity<List<Pais>> getAll() {
        return ResponseEntity.ok(paisServiceImp.allPais());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pais> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(paisServiceImp.getByIdPais(id));
    }

    @PostMapping
    public ResponseEntity<Pais> savePais(@RequestBody Pais pais) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paisServiceImp.savePais(pais));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePais(@PathVariable Long id) {
        paisServiceImp.deletePais(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Pais> updatePais(@RequestBody Pais pais) {
        return ResponseEntity.ok(paisServiceImp.updatePais(pais));
    }
}