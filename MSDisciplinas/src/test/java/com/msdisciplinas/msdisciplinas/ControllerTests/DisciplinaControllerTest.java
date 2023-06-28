package com.msdisciplinas.msdisciplinas.ControllerTests;

import com.ms.common.DTO.BaseDTO;
import com.msdisciplinas.msdisciplinas.Controller.DisciplinaController;
import com.msdisciplinas.msdisciplinas.DTO.DadosCadastroDisciplinaDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosCadeiraAlunoDTO;
import com.msdisciplinas.msdisciplinas.DTO.DadosMatriculaDTO;
import com.msdisciplinas.msdisciplinas.ENUM.CadastroDisciplinaEnum;
import com.msdisciplinas.msdisciplinas.Model.Disciplina;
import com.msdisciplinas.msdisciplinas.Service.DisciplinaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DisciplinaControllerTest {

    @Mock
    private DisciplinaService disciplinaService;

    @InjectMocks
    private DisciplinaController disciplinaController;

    @Test
    public void testAdicionarDisciplinaSuccess() {
        
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        when(disciplinaService.adicionarDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.ADICIONOU);

        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        
        assertTrue(result.sucess());
        assertEquals("Disciplina cadastrada com sucesso.", result.response());
    }

    @Test
    public void testAdicionarDisciplinaDisciplinaJaComTurma() {
        
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1);
        disciplina.setCodigo_disciplina(100);
        disciplina.setNome("Disciplina A");
        disciplina.setHorario("A");

        when(disciplinaService.adicionarDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.DISCIPLINA_JA_COM_TURMA);

        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        
        assertFalse(result.sucess());
        assertEquals("Disciplina já cadastrada nesta turma.", result.response());
    }

    @Test
    public void testAdicionarDisciplinaSemTurmas() {
        
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1);
        disciplina.setCodigo_disciplina(100);
        disciplina.setNome("Disciplina A");
        disciplina.setHorario("A");

        when(disciplinaService.adicionarDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.SEM_TURMAS);

        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        
        assertFalse(result.sucess());
        assertEquals("Não foi Encontrada nenhuma turma com este código.", result.response());
    }

    @Test
    public void testObtemCadeirasAlunoSuccess() {
        
        long codMatric = 100;

        List<DadosCadeiraAlunoDTO> cadeirasAluno = new ArrayList<>();
        cadeirasAluno.add(new DadosCadeiraAlunoDTO(1, 100));
        cadeirasAluno.add(new DadosCadeiraAlunoDTO(2, 200));

        when(disciplinaService.obterListaMatriculasAluno(codMatric)).thenReturn(cadeirasAluno);

        BaseDTO result = disciplinaController.obterCadeirasAluno(codMatric);

        
        assertTrue(result.sucess());
        assertEquals(cadeirasAluno, result.response());
    }

    @Test
    public void testObtemCadeirasAlunoNoCadeirasFound() {
        
        long codMatric = 100;

        List<DadosCadeiraAlunoDTO> cadeirasAluno = new ArrayList<>();

        when(disciplinaService.obterListaMatriculasAluno(codMatric)).thenReturn(cadeirasAluno);

        BaseDTO result = disciplinaController.obterCadeirasAluno(codMatric);

        
        assertFalse(result.sucess());
        assertEquals("Cadeiras não encontradas para o aluno.", result.response());
    }

    @Test
    public void testObtemCadeirasAlunoException() {
        
        long codMatric = 100;

        when(disciplinaService.obterListaMatriculasAluno(codMatric)).thenThrow(new RuntimeException("Erro ao obter as cadeiras do aluno."));

        BaseDTO result = disciplinaController.obterCadeirasAluno(codMatric);

        
        assertFalse(result.sucess());
        assertEquals("Erro ao obter as cadeiras do aluno.", result.response());
    }

    @Test
    public void testObtemAlunosPorCadeiraSuccess() {
        
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        List<String> alunos = Arrays.asList("Alice", "Bob", "Charlie");

        when(disciplinaService.encontrarAlunosPorCadeira(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenReturn(new BaseDTO(true, alunos));

        BaseDTO result = disciplinaController.obterAlunosPorCadeira(dadosCadeiraAlunoDTO);

        
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testObtemAlunosPorCadeiraNoAlunosFound() {
        
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        List<String> alunos = Collections.emptyList();

        when(disciplinaService.encontrarAlunosPorCadeira(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenReturn(new BaseDTO(true, alunos));

        BaseDTO result = disciplinaController.obterAlunosPorCadeira(dadosCadeiraAlunoDTO);

        
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testObtemAlunosPorCadeiraException() {
        
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        when(disciplinaService.encontrarAlunosPorCadeira(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenThrow(new RuntimeException("Erro ao obter os alunos da cadeira."));

        BaseDTO result = disciplinaController.obterAlunosPorCadeira(dadosCadeiraAlunoDTO);

        
        assertFalse(result.sucess());
        assertEquals("Erro ao obter os alunos da cadeira.", result.response());
    }

    @Test
    public void testMatriculaAlunoSuccess() throws Exception {
        
        DadosMatriculaDTO dadosMatriculaDTO = new DadosMatriculaDTO(12345, 100, 200);

        when(disciplinaService.matricularAluno(dadosMatriculaDTO))
                .thenReturn(new BaseDTO(true, "Aluno matriculado com sucesso."));

        BaseDTO result = disciplinaController.matricularAluno(dadosMatriculaDTO);

        
        assertTrue(result.sucess());
        assertEquals("Aluno matriculado com sucesso.", result.response());
    }

    @Test
    public void testMatriculaAlunoException() throws Exception {
    
        DadosMatriculaDTO dadosMatriculaDTO = new DadosMatriculaDTO(12345, 100, 200);

        when(disciplinaService.matricularAluno(dadosMatriculaDTO))
                .thenThrow(new RuntimeException("Erro ao matricular o aluno."));

        BaseDTO result = disciplinaController.matricularAluno(dadosMatriculaDTO);

        
        assertFalse(result.sucess());
        assertEquals("Erro ao matricular o aluno.", result.response());
    }
}
