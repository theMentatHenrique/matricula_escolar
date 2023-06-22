package com.msdisciplinas.msdisciplinas.Repository;


import com.msdisciplinas.msdisciplinas.Model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("disciplinaRepository")
public interface IRepoDisciplina extends JpaRepository<Disciplina, Long> {
    @Query("select d from Disciplina d where d.codigo_disciplina = ?1")
    List<Disciplina> findDisciplinas(long l);

    @Query("select d.codigo_disciplina from Disciplina d where d.id = ?1")
    long findCodDisciplina(long codigo_disciplina);
}