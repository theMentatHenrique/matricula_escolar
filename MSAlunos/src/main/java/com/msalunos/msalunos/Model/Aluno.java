package com.msalunos.msalunos.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Aluno")
@EqualsAndHashCode(of = "num_matricula")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Aluno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_matricula")
    @JsonIgnore
    long num_matricula;

    @NotEmpty
    @Column(name = "nome")
    private String nome;

    @Column(name = "num_documento")
    @NotEmpty
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$")
    private String num_documento;

    @NotEmpty
    @Column(name = "endereco")
    private String endereco;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(

            name = "matricula",
            joinColumns = @JoinColumn(name = "aluno_num_matricula"),
            inverseJoinColumns = @JoinColumn(name = "cadeira_id")
    )
    private List<Cadeira> matriculas = new ArrayList<>();

    public String getNome() {
        return this.nome;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public List<Cadeira> getMatriculas() {
        return this.matriculas;
    }

    public long getNum_matricula() {
        return this.num_matricula;
    }

    public void setNum_matricula(long num_matricula) {
        this.num_matricula = num_matricula;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setNum_documento(String num_documento) {
        this.num_documento = num_documento;
    }

    public void setMatriculas(List<Cadeira> matriculas) {
        this.matriculas = matriculas;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

}
