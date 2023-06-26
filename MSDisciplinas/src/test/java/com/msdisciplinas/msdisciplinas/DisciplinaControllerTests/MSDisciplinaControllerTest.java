package com.msdisciplinas.msdisciplinas.DisciplinaControllerTests;

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
public class MSDisciplinaControllerTest {

    @Mock
    private DisciplinaService disciplinaService;

    @InjectMocks
    private DisciplinaController disciplinaController;

    @Test
    public void testAdicionarDisciplina_Success() {
        // Arrange
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        when(disciplinaService.adicionaDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.ADICIONOU);

        // Act
        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        // Assert
        assertTrue(result.sucess());
        assertEquals("Disciplina cadastrada com sucesso.", result.response());
    }

    @Test
    public void testAdicionarDisciplina_DisciplinaJaComTurma() {
        // Arrange
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1);
        disciplina.setCodigo_disciplina(100);
        disciplina.setNome("Disciplina A");
        disciplina.setHorario("A");

        when(disciplinaService.adicionaDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.DISCIPLINA_JA_COM_TURMA);

        // Act
        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Disciplina já cadastrada nesta turma.", result.response());
    }

    @Test
    public void testAdicionarDisciplina_SemTurmas() {
        // Arrange
        DadosCadastroDisciplinaDTO dadosCadastroDisciplinaDTO = new DadosCadastroDisciplinaDTO(100, "Disciplina A", "A", 100);

        Disciplina disciplina = new Disciplina();
        disciplina.setId(1);
        disciplina.setCodigo_disciplina(100);
        disciplina.setNome("Disciplina A");
        disciplina.setHorario("A");

        when(disciplinaService.adicionaDisciplina(any(Disciplina.class), any(Long.class))).thenReturn(CadastroDisciplinaEnum.SEM_TURMAS);

        // Act
        BaseDTO result = disciplinaController.adicionarDisciplina(dadosCadastroDisciplinaDTO);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Não foi Encontrada nenhuma turma com este código.", result.response());
    }

    @Test
    public void testObtemCadeirasAluno_Success() {
        // Arrange
        long codMatric = 100;

        List<DadosCadeiraAlunoDTO> cadeirasAluno = new ArrayList<>();
        cadeirasAluno.add(new DadosCadeiraAlunoDTO(1, 100));
        cadeirasAluno.add(new DadosCadeiraAlunoDTO(2, 200));

        when(disciplinaService.obtemListaMatriculasAluno(codMatric)).thenReturn(cadeirasAluno);

        // Act
        BaseDTO result = disciplinaController.obtemCadeirasAluno(codMatric);

        // Assert
        assertTrue(result.sucess());
        assertEquals(cadeirasAluno, result.response());
    }

    @Test
    public void testObtemCadeirasAluno_NoCadeirasFound() {
        // Arrange
        long codMatric = 100;

        List<DadosCadeiraAlunoDTO> cadeirasAluno = new ArrayList<>();

        when(disciplinaService.obtemListaMatriculasAluno(codMatric)).thenReturn(cadeirasAluno);

        // Act
        BaseDTO result = disciplinaController.obtemCadeirasAluno(codMatric);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Cadeiras não encontradas para o aluno.", result.response());
    }

    @Test
    public void testObtemCadeirasAluno_Exception() {
        // Arrange
        long codMatric = 100;

        when(disciplinaService.obtemListaMatriculasAluno(codMatric)).thenThrow(new RuntimeException("Erro ao obter as cadeiras do aluno."));

        // Act
        BaseDTO result = disciplinaController.obtemCadeirasAluno(codMatric);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao obter as cadeiras do aluno.", result.response());
    }

    @Test
    public void testObtemAlunosPorCadeira_Success() {
        // Arrange
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        List<String> alunos = Arrays.asList("Alice", "Bob", "Charlie");

        when(disciplinaService.encontraAlunos(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenReturn(new BaseDTO(true, alunos));

        // Act
        BaseDTO result = disciplinaController.obtemAlunosPorCadeira(dadosCadeiraAlunoDTO);

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testObtemAlunosPorCadeira_NoAlunosFound() {
        // Arrange
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        List<String> alunos = Collections.emptyList();

        when(disciplinaService.encontraAlunos(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenReturn(new BaseDTO(true, alunos));

        // Act
        BaseDTO result = disciplinaController.obtemAlunosPorCadeira(dadosCadeiraAlunoDTO);

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testObtemAlunosPorCadeira_Exception() {
        // Arrange
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        when(disciplinaService.encontraAlunos(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenThrow(new RuntimeException("Erro ao obter os alunos da cadeira."));

        // Act
        BaseDTO result = disciplinaController.obtemAlunosPorCadeira(dadosCadeiraAlunoDTO);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao obter os alunos da cadeira.", result.response());
    }

    @Test
    public void testObtemAlunosPorCadeira_Exception1() {
        // Arrange
        DadosCadeiraAlunoDTO dadosCadeiraAlunoDTO = new DadosCadeiraAlunoDTO(1, 100);

        when(disciplinaService.encontraAlunos(dadosCadeiraAlunoDTO.codigo_disciplina(), dadosCadeiraAlunoDTO.cod_turma()))
                .thenThrow(new RuntimeException("Erro ao obter os alunos da cadeira."));

        // Act
        BaseDTO result = disciplinaController.obtemAlunosPorCadeira(dadosCadeiraAlunoDTO);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao obter os alunos da cadeira.", result.response());
    }

    @Test
    public void testMatriculaAluno_Success() throws Exception {
        // Arrange
        DadosMatriculaDTO dadosMatriculaDTO = new DadosMatriculaDTO(12345, 100, 200);

        when(disciplinaService.matricularAluno(dadosMatriculaDTO))
                .thenReturn(new BaseDTO(true, "Aluno matriculado com sucesso."));

        // Act
        BaseDTO result = disciplinaController.matriculaAluno(dadosMatriculaDTO);

        // Assert
        assertTrue(result.sucess());
        assertEquals("Aluno matriculado com sucesso.", result.response());
    }

    @Test
    public void testMatriculaAluno_Exception() throws Exception {
        // Arrange
        DadosMatriculaDTO dadosMatriculaDTO = new DadosMatriculaDTO(12345, 100, 200);

        when(disciplinaService.matricularAluno(dadosMatriculaDTO))
                .thenThrow(new RuntimeException("Erro ao matricular o aluno."));

        // Act
        BaseDTO result = disciplinaController.matriculaAluno(dadosMatriculaDTO);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao matricular o aluno.", result.response());
    }
}
