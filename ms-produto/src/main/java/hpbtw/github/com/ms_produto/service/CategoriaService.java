package hpbtw.github.com.ms_produto.service;

import hpbtw.github.com.ms_produto.dto.CategoriaRequestDTO;
import hpbtw.github.com.ms_produto.dto.CategoriaResponseDTO;
import hpbtw.github.com.ms_produto.entities.Categoria;
import hpbtw.github.com.ms_produto.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_produto.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {
    private CategoriaRepository repo;

    @Transactional
    public CategoriaResponseDTO saveCategoria(CategoriaRequestDTO input) {
        Categoria c = new Categoria();
        copyInputToCategoria(input, c);
        return new CategoriaResponseDTO(repo.save(c));
    }

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
