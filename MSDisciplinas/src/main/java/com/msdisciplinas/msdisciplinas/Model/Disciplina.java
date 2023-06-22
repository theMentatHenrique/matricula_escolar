package com.msdisciplinas.msdisciplinas.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Disciplina")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Disciplina implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "codigo_disciplina")
    private long codigo_disciplina;

    @Column(name = "nome")
    @NotEmpty
    private String nome;


    @Column(name = "horario")
    private String horario;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(

            name = "Cadeira",
            joinColumns = @JoinColumn(name = "disciplina_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    private List<Turma> turmas = new ArrayList<>();


    public List<Turma> getTurma() {
        return this.turmas;
    }

    public void setTurma(List<Turma> turmas) {
        this.turmas = turmas;
    }

}