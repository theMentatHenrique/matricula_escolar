package com.msalunos.msalunos.Controller;

import com.ms.common.DTO.BaseDTO;
import com.ms.common.DTO.ListaMatriculaDTO;
import com.ms.common.DTO.MatriculaAlunoDTO;
import com.msalunos.msalunos.DTO.DadosCadastroAlunoDTO;
import com.msalunos.msalunos.Model.Aluno;
import com.msalunos.msalunos.Model.Cadeira;
import com.msalunos.msalunos.Repository.IRepoAluno;
import com.msalunos.msalunos.Repository.IRepoCadeira;
import com.msalunos.msalunos.Service.AlunoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AlunoController {

    @Autowired
    private IRepoAluno iRepoAluno;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private IRepoCadeira iRepoCadeira;

    @PostMapping("/adicionar_aluno")
    public BaseDTO adicionarAluno(@RequestBody @Valid DadosCadastroAlunoDTO dadosCadastroAlunoDTO) {
        try{
            Aluno aluno = new Aluno();
            BeanUtils.copyProperties(dadosCadastroAlunoDTO ,aluno);
            iRepoAluno.save(aluno);
            return new BaseDTO(true, "Aluno cadastrado com sucesso.");
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/listar_alunos/{nome}")
    public BaseDTO listarAlunosPorNome(@PathVariable @NotEmpty String nome) {
        try {
            List<Aluno> alunosPorNome = alunoService.getAlunosPorNome(nome);
            if (alunosPorNome.size() == 0) {
                throw new Exception("Não foram encontrados alunos com este nome.");
            }
            return new BaseDTO(true, alunosPorNome);
        } catch (Exception e) {
            return new BaseDTO(false,e.getMessage());
        }
    }

    @GetMapping("/listar_todos_alunos")
    public BaseDTO listarTodosAlunos() {
        try {
            List<Aluno> alunos = iRepoAluno.findAll();
            if (alunos.isEmpty()) {
                throw new Exception("Não há alunos cadastrados.");
            }
            return new BaseDTO(true, alunos);
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @PostMapping("/matricular_aluno/{numMatric}/{disciplina_id}/{turma_id}")
    public BaseDTO matricularAluno(@PathVariable @NotEmpty long numMatric, @PathVariable long disciplina_id, @PathVariable long turma_id) {
        try{
            if(!alunoService.matricularAluno(numMatric, disciplina_id, turma_id)) {
                throw new Exception("Não foi possível matricular o aluno");
            }
            return new BaseDTO(true,"Aluno matriculado com sucesso.");
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/obter_aluno/{numMatric}")
    public BaseDTO obterAluno(@PathVariable @NotEmpty long numMatric) {
        try {
            Optional<Aluno> alunoObtido = iRepoAluno.findById(numMatric);
            if (alunoObtido.isEmpty()) {
                throw new Exception("Não foi encontrado nenhum aluno com este número de matricula.");
            }

            return new BaseDTO(true, alunoObtido);

        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }
    }

    @GetMapping("/obter_cadeiras_aluno/{cod_matric}")
    public ListaMatriculaDTO obterCadeirasAluno(@PathVariable long cod_matric) {
        try{
            Optional<Aluno> aluno = iRepoAluno.findById(cod_matric);
            if (aluno.isEmpty()) {
                throw new Exception();
            }
            List<MatriculaAlunoDTO> matriculaAlunoDTOList = new ArrayList<>();
            for (Cadeira cadeira : aluno.get().getMatriculas()) {
                MatriculaAlunoDTO matriculaAlunoDTO= new MatriculaAlunoDTO(cadeira.getDisciplina_id(), cadeira.getTurma_id());
                matriculaAlunoDTOList.add(matriculaAlunoDTO);
            }
            return new ListaMatriculaDTO(true,matriculaAlunoDTOList);
        } catch (Exception e ) {
            return new ListaMatriculaDTO(false,null);
        }
    }

    @GetMapping("/obter_alunos_por_cadeira/{disciplina_id}/{turma_id}")
    public BaseDTO obterAlunosPorCadeira(@PathVariable long disciplina_id, @PathVariable long turma_id) {
        try {
            Cadeira cadeira = iRepoCadeira.findByFk(disciplina_id, turma_id);

            if (cadeira == null || cadeira.getAlunos() == null || cadeira.getAlunos().isEmpty()) {
                throw new Exception("Alunos não encontrados para esta cadeira.");
            }
            List<Aluno> alunos = cadeira.getAlunos();
            return new BaseDTO(true, alunos);
        } catch (Exception e) {
            return new BaseDTO(false, e.getMessage());
        }

    }
}