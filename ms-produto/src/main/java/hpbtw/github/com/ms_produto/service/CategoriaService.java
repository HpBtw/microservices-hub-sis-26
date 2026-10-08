package hpbtw.github.com.ms_produto.service;

import hpbtw.github.com.ms_produto.dto.CategoriaRequestDTO;
import hpbtw.github.com.ms_produto.dto.CategoriaResponseDTO;
import hpbtw.github.com.ms_produto.entities.Categoria;
import hpbtw.github.com.ms_produto.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_produto.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository repo;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAllCategorias() {
        return repo.findAll().stream().map(CategoriaResponseDTO::new).toList();
    }

    @Transactional
    public CategoriaResponseDTO saveCategoria(CategoriaRequestDTO input) {
        Categoria c = new Categoria();
        copyInputToCategoria(input, c);
        return new CategoriaResponseDTO(repo.save(c));
    }

    @Transactional
    public CategoriaResponseDTO updateCategoria(Long id, CategoriaRequestDTO input) {
        try {
            Categoria c = repo.getReferenceById(id);
            copyInputToCategoria(input, c);
            return new CategoriaResponseDTO(repo.save(c));
        } catch (EntityNotFoundException e ) {
            throw new ResourceNotFoundException("Recurso não encontrado. ID: " + id);
        }
    }

    private void copyInputToCategoria(CategoriaRequestDTO input, Categoria c) {
        c.setNome(input.getNome());
    }
}
