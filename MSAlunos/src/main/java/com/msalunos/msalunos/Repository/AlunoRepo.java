package com.msalunos.msalunos.Repository;

import com.msalunos.msalunos.Model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepo extends JpaRepository<Aluno, Long> {
}
