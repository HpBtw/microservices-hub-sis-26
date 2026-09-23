package hpbtw.github.com.ms_produto.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_categoria")
public class Categoria {
    @Id
    private Long id;
    private String nome;
}
