package hpbtw.github.com.ms_produto.dto;

import hpbtw.github.com.ms_produto.entities.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoriaRequestDTO {
    @NotBlank(message = "Campo nome é requerido.") @Size(min = 3, max = 100, message = "Campo nome deve ter entre 3 a 100 caracteres.")
    @Schema(example = "Ferramentas")
    private String nome;

    public CategoriaRequestDTO(Categoria c) {
        this.nome = c.getNome();
    }
}
