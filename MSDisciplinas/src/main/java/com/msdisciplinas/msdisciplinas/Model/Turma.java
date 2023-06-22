package com.msdisciplinas.msdisciplinas.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Turma")
public class Turma {


    @Column(name = "id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name= "cod_turma")
    private Long cod_turma;

    @JsonIgnore
    @ManyToMany(mappedBy = "turmas")
    private List<Disciplina> disciplinas = new ArrayList<>();

}
