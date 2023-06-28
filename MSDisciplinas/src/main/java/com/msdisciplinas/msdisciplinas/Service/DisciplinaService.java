package com.msdisciplinas.msdisciplinas.Service;

import com.ms.common.DTO.BaseDTO;
import com.ms.common.DTO.ListaMatriculaDTO;
import com.ms.common.DTO.MatriculaAlunoDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosMatriculaDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosCadeiraAlunoDTO;
import com.msdisciplinas.msdisciplinas.DTO.IdsTurmaDisciplinaDTO;
import com.msdisciplinas.msdisciplinas.ENUM.CadastroDisciplinaEnum;
import com.msdisciplinas.msdisciplinas.Model.Disciplina;
import com.msdisciplinas.msdisciplinas.Model.Turma;
import com.msdisciplinas.msdisciplinas.Repository.IRepoDisciplina;
import com.msdisciplinas.msdisciplinas.Repository.IRepoTurma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("DisciplinaService")
public class DisciplinaService {

    @Autowired
    private IRepoDisciplina iRepoDisciplina;

    @Autowired
    private IRepoTurma iRepoTurma;

    public CadastroDisciplinaEnum adicionarDisciplina(Disciplina disciplina, long cod_turma) {
        try {
            Optional<Turma> turma = Optional.ofNullable(iRepoTurma.findByCode(cod_turma));
            if (turma.isEmpty()) {
                return CadastroDisciplinaEnum.SEM_TURMAS;
            }
            IdsTurmaDisciplinaDTO idsCadeiraAlunoDTO = obtemDisciplinaComTurma(disciplina.getCodigo_disciplina(), cod_turma);
            if (idsCadeiraAlunoDTO != null) {
                return CadastroDisciplinaEnum.DISCIPLINA_JA_COM_TURMA;
            }

            Turma turmaReference = iRepoTurma.getReferenceById(turma.get().getId());
            disciplina.getTurmas().add(turmaReference);
            iRepoDisciplina.save(disciplina);
            return CadastroDisciplinaEnum.ADICIONOU;
        } catch (Exception e ) {
            return CadastroDisciplinaEnum.ERRO;
        }
    }

    public List<DadosCadeiraAlunoDTO> obterListaMatriculasAluno(long codMatric) {
        try{
            ListaMatriculaDTO listaMatriculaDTO = listarCadeirasAluno(codMatric);
            if (!listaMatriculaDTO.sucesso()) {return null;}

            List<DadosCadeiraAlunoDTO> listaDiscTurmas = new ArrayList<>();
            for (MatriculaAlunoDTO matricula: listaMatriculaDTO.listaMatriculas()) {
                long codTurma = iRepoTurma.findCodTurma(matricula.turma_id());
                Optional<Disciplina> disciplina = iRepoDisciplina.findById(matricula.disciplina_id());
                if (disciplina.isEmpty() || codTurma == 0) {
                    return null;
                }
                listaDiscTurmas.add(new DadosCadeiraAlunoDTO(disciplina.get().getCodigo_disciplina(), codTurma));
            }

            return listaDiscTurmas;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private  ListaMatriculaDTO listarCadeirasAluno(long codMatric) {
        String url = "http://localhost:8080/obter_cadeiras_aluno/";
        url += String.valueOf(codMatric);
        RestTemplate restTemplate = new RestTemplate();
        ListaMatriculaDTO listaMatriculaDTO = restTemplate.getForObject(url, ListaMatriculaDTO.class);
        return listaMatriculaDTO;
    }
    
    public BaseDTO encontrarAlunosPorCadeira(long codDisciplina, long codTurma) {
        try{
            IdsTurmaDisciplinaDTO tuplaIdsDiscTurma = obtemDisciplinaComTurma(codDisciplina, codTurma);
            if (tuplaIdsDiscTurma == null) {
                return new BaseDTO(false, "Não foi encontrado nenhum aluno nesta cadeira.");
            }
            return buscarAlunos(tuplaIdsDiscTurma.disciplina_id(), tuplaIdsDiscTurma.turma_id());
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    private BaseDTO buscarAlunos(long disciplinaId, long turmaId) {
        try{
            String url = "http://localhost:8080/obter_alunos_por_cadeira/";
            url += String.valueOf(disciplinaId);
            url += "/" + String.valueOf(turmaId);
            RestTemplate restTemplate = new RestTemplate();
            BaseDTO dtoBase = restTemplate.getForObject(url, BaseDTO.class);
            if(dtoBase == null || !dtoBase.sucess()) {
                throw new Exception("Não foi possível acessar a lista de alunos.");
            }
            return dtoBase;
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());

        }
    }

    private IdsTurmaDisciplinaDTO obtemDisciplinaComTurma(long codDisciplina, long codTurma) {
        List<Disciplina> disciplinas = iRepoDisciplina.findDisciplinas(codDisciplina);
        for (Disciplina disciplina : disciplinas) {
            for(Turma turma : disciplina.getTurma()) {
                if (turma.getCod_turma() == codTurma) {
                    return new IdsTurmaDisciplinaDTO(disciplina.getId(), turma.getId());
                }
            }
        }
        return null;
    }

    public BaseDTO matricularAluno(DadosMatriculaDTO dadosMatriculaDTO) throws Exception {
        try {
            IdsTurmaDisciplinaDTO tuplaIdsDiscTurma = encontrarDisciplinaComTurma(dadosMatriculaDTO);
            return matricularAluno(dadosMatriculaDTO, tuplaIdsDiscTurma);
        } catch (Exception e) {
            return new BaseDTO(true,e.getMessage());
        }
    }

    private IdsTurmaDisciplinaDTO encontrarDisciplinaComTurma(DadosMatriculaDTO dadosMatriculaDTO) {
        List<Disciplina> disciplinas =  iRepoDisciplina.findDisciplinas(dadosMatriculaDTO.codigo_disciplina());
        long turmaId = 0;
        long disciplinaId = 0;
        for(Disciplina d : disciplinas) {
            for(Turma t : d.getTurmas()) {
                if (t.getCod_turma() == dadosMatriculaDTO.cod_turma()){

                    turmaId = t.getId();
                    disciplinaId = d.getId();
                }
            }
        }
        return new IdsTurmaDisciplinaDTO(disciplinaId, turmaId);
    }

    private BaseDTO matricularAluno(DadosMatriculaDTO dadosMatriculaDTO, IdsTurmaDisciplinaDTO tuplaIdsDiscTurma) {
        try{
            String url = formatarRequisicaoMatricularAluno(dadosMatriculaDTO.num_matricula(), tuplaIdsDiscTurma.disciplina_id(), tuplaIdsDiscTurma.turma_id());
            RestTemplate restTemplate = new RestTemplate();
            BaseDTO dtoBase = new BaseDTO(false, "");
            dtoBase = restTemplate.postForObject(url, dtoBase, BaseDTO.class);
            if(!dtoBase.sucess()) {
                throw new Exception("Não foi possível Matricular o aluno");
            }
            return dtoBase;
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    private String formatarRequisicaoMatricularAluno(long num_matric, long disciplina_id, long turma_id) {
        String url = "http://localhost:8080/matricular_aluno/";
        url = url + String.valueOf(num_matric);
        url = url + "/" + String.valueOf(disciplina_id);
        url = url + "/" + String.valueOf(turma_id);
        return url;
    }
}