package com.msalunos.msalunos.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cadeira")
@EqualsAndHashCode(of = "id")
@Data
public class Cadeira implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Long id;

    @Column(name = "disciplina_id")
    @NotEmpty
    private long disciplina_id;

    @Column(name = "turma_id")
    @NotEmpty
    private long turma_id;

    @JsonIgnore
    @ManyToMany(mappedBy = "matriculas")
    private List<Aluno> alunos = new ArrayList<>();

    public long getDisciplina_id() {
        return disciplina_id;
    }

    public long getTurma_id() {
        return turma_id;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDisciplina_id(long disciplina_id) {
        this.disciplina_id = disciplina_id;
    }

    public void setTurma_id(long turma_id) {
        this.turma_id = turma_id;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void addAluno(Aluno aluno) {
        this.alunos.add(aluno);
    }

    public Long getId() {
        return id;
    }
}
