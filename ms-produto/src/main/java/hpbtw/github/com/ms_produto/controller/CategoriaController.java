package hpbtw.github.com.ms_produto.controller;

import hpbtw.github.com.ms_produto.dto.CategoriaRequestDTO;
import hpbtw.github.com.ms_produto.dto.CategoriaResponseDTO;
import hpbtw.github.com.ms_produto.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> createCategoria(@RequestBody CategoriaRequestDTO input) {
        CategoriaResponseDTO response = service.saveCategoria(input);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> updateCategoria(Long id, @RequestBody CategoriaRequestDTO input) {
        CategoriaResponseDTO response = service.updateCategoria(id, input);
        return ResponseEntity.ok(response);
    }
}