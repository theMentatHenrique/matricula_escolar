package com.msalunos.msalunos.Service;


import com.msalunos.msalunos.Model.Aluno;
import com.msalunos.msalunos.Model.Cadeira;
import com.msalunos.msalunos.Repository.AlunoRepo;
import com.msalunos.msalunos.Repository.CadeiraRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("AlunoService")
public class AlunoService {

    @Autowired
    private AlunoRepo alunoRepo;

    @Autowired
    private CadeiraRepo cadeiraRepo;

    public List<Aluno> getAlunosPorNome(String nome) {
        try {
            List<Aluno> alunosMatch = new ArrayList<>();
            for (Aluno aluno : alunoRepo.findAll()) {

                if (nome.trim().length() <= aluno.getNome().trim().length()) {
                    String nomeRecortado = aluno.getNome().trim().substring(0, nome.length());
                    if (nomeRecortado.equals(nome)) {
                        alunosMatch.add(aluno);
                    }
                }
            }
            return alunosMatch;
        } catch (Exception e) {
            return new ArrayList<Aluno>();
        }
    }

    public Cadeira cadeiraExistente(long disciplina_id, long turma_id){
        try{
            return cadeiraRepo.findByFk(disciplina_id, turma_id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean matricularAluno(long numMatric, long disciplina_id, long turma_id) {
        try {
            Aluno aluno = alunoRepo.getReferenceById(numMatric);
            Cadeira cadeira = cadeiraRepo.findByFk(disciplina_id, turma_id);
            if (cadeira == null) {
                return false;
            }
            aluno.getMatriculas().add(cadeira);
            alunoRepo.save(aluno);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
