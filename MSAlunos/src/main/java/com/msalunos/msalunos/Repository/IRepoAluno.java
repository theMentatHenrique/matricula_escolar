package com.msalunos.msalunos.Repository;

import com.msalunos.msalunos.Model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepoAluno extends JpaRepository<Aluno, Long> {
}
