package com.msdisciplinas.msdisciplinas.Controller;

import com.ms.common.DTO.BaseDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosCadastroDisciplinaDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosCadeiraAlunoDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosMatriculaDTO;
import com.msdisciplinas.msdisciplinas.ENUM.CadastroDisciplinaEnum;
import com.msdisciplinas.msdisciplinas.Model.Disciplina;
import com.msdisciplinas.msdisciplinas.Service.DisciplinaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping("/adicionarDisciplina")
    public BaseDTO adicionarDisciplina(@RequestBody @Valid DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO) {
        try {
            Disciplina disciplina = new Disciplina();
            BeanUtils.copyProperties(dadosCadastroDisciplinaDTO, disciplina);
            CadastroDisciplinaEnum adicionou = disciplinaService.adicionarDisciplina(disciplina, dadosCadastroDisciplinaDTO.cod_turma());

            if (adicionou.equals(CadastroDisciplinaEnum.DISCIPLINA_JA_COM_TURMA)) {
                throw new Exception("Disciplina já cadastrada nesta turma.");

            }
            if (adicionou.equals(CadastroDisciplinaEnum.SEM_TURMAS)) {
                throw new Exception("Não foi Encontrada nenhuma turma com este código.");
            }
            if (adicionou.equals(CadastroDisciplinaEnum.ERRO)) {
                throw new Exception("Não foi possível cadastrar a disciplina.");
            }
            return new BaseDTO(true, "Disciplina cadastrada com sucesso.");

        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }

    }

    @GetMapping("/obterCadeirasAluno/{cod_matric}")
    public BaseDTO obterCadeirasAluno(@PathVariable long cod_matric) {
        try{
            List<DadosCadeiraAlunoDTO> tuplaCodTurmaDisciplinaList = disciplinaService.obterListaMatriculasAluno(cod_matric);
            if (tuplaCodTurmaDisciplinaList.isEmpty()) {
                throw new Exception("Cadeiras não encontradas para o aluno.");
            }
            return new BaseDTO(true, tuplaCodTurmaDisciplinaList);

        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/obterAlunosPorCadeira")
    public BaseDTO obterAlunosPorCadeira(@RequestBody @NotNull DadosCadeiraAlunoDTO tuplaCodTurmaDisciplina) {
        try{
            return disciplinaService.encontrarAlunosPorCadeira(tuplaCodTurmaDisciplina.codigo_disciplina(), tuplaCodTurmaDisciplina.cod_turma());
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @PostMapping("/matricularAluno")
    public BaseDTO matricularAluno(@RequestBody @Valid DadosMatriculaDTO dadosMatriculaDTO) {
        try {
            return disciplinaService.matricularAluno(dadosMatriculaDTO);

        } catch(Exception e) {
            return new BaseDTO(false,e.getMessage());
        }
    }

}
