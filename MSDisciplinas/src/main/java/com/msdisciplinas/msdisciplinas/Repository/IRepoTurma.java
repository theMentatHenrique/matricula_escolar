package com.msdisciplinas.msdisciplinas.Repository;

import com.msdisciplinas.msdisciplinas.Model.Turma;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository("TurmaRepository")
public interface IRepoTurma extends JpaRepository<Turma, Long> {
    @Query("select t from Turma t where t.cod_turma = ?1")
    public Turma findByCode(Long codigo);

    @Query("select t.cod_turma from Turma t where t.id = ?1")
    long findCodTurma(long l);
}