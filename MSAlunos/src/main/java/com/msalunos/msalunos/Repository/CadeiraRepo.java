package com.msalunos.msalunos.Repository;

import com.msalunos.msalunos.Model.Cadeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CadeiraRepo extends JpaRepository<Cadeira, Long> {
    @Query("select c from Cadeira c where c.disciplina_id = ?1 and c.turma_id =?2")
    Cadeira findByFk(long disciplina_id, long turma_id);
}
