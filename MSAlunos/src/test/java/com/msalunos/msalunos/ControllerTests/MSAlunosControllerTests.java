package com.msalunos.msalunos.ControllerTests;
import com.ms.common.DTO.BaseDTO;
import com.ms.common.DTO.ListaMatriculaDTO;
import com.msalunos.msalunos.Controller.AlunoController;
import com.msalunos.msalunos.DTO.DadosCadastroAlunoDTO;
import com.msalunos.msalunos.Model.Aluno;
import com.msalunos.msalunos.Model.Cadeira;
import com.msalunos.msalunos.Repository.AlunoRepo;
import com.msalunos.msalunos.Repository.CadeiraRepo;
import com.msalunos.msalunos.Service.AlunoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MSAlunosControllerTests {
    @Mock
    private AlunoRepo alunoRepo;

    @Mock
    private CadeiraRepo cadeiraRepo;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private AlunoController controller;


    @Test
    public void testAdicionaAluno_Success() {
        // Arrange
        DadosCadastroAlunoDTO dto = new DadosCadastroAlunoDTO("John Doe", "123.112.222.32","rua chico pedor" );

        Aluno aluno = new Aluno();
        BeanUtils.copyProperties(dto, aluno);

        when(alunoRepo.save(any(Aluno.class))).thenReturn(aluno);

        // Act
        BaseDTO result = controller.adicionaAluno(dto);

        // Assert
        assertTrue(result.sucess());
        assertEquals("Aluno cadastrado com sucesso.", result.response());
    }

    @Test
    public void testAdicionaAluno_Failure() {
        // Arrange
        DadosCadastroAlunoDTO dto = new DadosCadastroAlunoDTO("jon dee", "111.111.111-11", "efwef");

        when(alunoRepo.save(any(Aluno.class))).thenThrow(new RuntimeException("Erro ao cadastrar o aluno."));

        // Act
        BaseDTO result = controller.adicionaAluno(dto);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao cadastrar o aluno.", result.response());
    }

    @Test
    public void testGetAlunosPorNome_Success() {
        // Arrange
        String nome = "John Doe";

        List<Aluno> alunosPorNome = new ArrayList<>();
        Aluno a = new Aluno();
        a.setNome("John Doe");
        alunosPorNome.add(a);

        when(alunoService.getAlunosPorNome(anyString())).thenReturn(alunosPorNome);

        // Act
        BaseDTO result = controller.getAlunosPorNome(nome);

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunosPorNome, result.response());
    }

    @Test
    public void testGetAlunosPorNome_NoMatchingAlunos() {
        // Arrange
        String nome = "Jane Smith";

        when(alunoService.getAlunosPorNome(anyString())).thenReturn(new ArrayList<>());

        // Act
        BaseDTO result = controller.getAlunosPorNome(nome);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Não foram encontrados alunos com este nome.", result.response());
    }

    @Test
    public void testGetAlunosPorNome_NoMatchingAlunos_parte_nome() {
        // Arrange
        String nome = "Jane";
        Aluno jane = new Aluno();
        Aluno janeSmith = new Aluno();
        jane.setNome("Jane Smith");
        janeSmith.setNome("Jane Smith feijó");
        List<Aluno> alunos = Arrays.asList(jane, janeSmith);
        when(alunoService.getAlunosPorNome(anyString())).thenReturn(alunos);

        // Act
        BaseDTO result = controller.getAlunosPorNome(nome);

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testListaTodosAlunos_Success() {
        // Arrange
        List<Aluno> alunos = Arrays.asList(new Aluno(), new Aluno(), new Aluno());
        when(alunoRepo.findAll()).thenReturn(alunos);

        // Act
        BaseDTO result = controller.listaTodosAlunos();

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testListaTodosAlunos_NoAlunosCadastrados() {
        // Arrange
        when(alunoRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        BaseDTO result = controller.listaTodosAlunos();

        // Assert
        assertFalse(result.sucess());
        assertEquals("Não há alunos cadastrados.", result.response());
    }

    @Test
    public void testMatriculaAluno_Success() {
        // Arrange
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        Aluno aluno = new Aluno();
        aluno.setNum_matricula(numMatric);

        Cadeira cadeira = new Cadeira();
        cadeira.setId(disciplinaId);
        cadeira.setTurma_id(turmaId);

        when(alunoService.matricularAluno(numMatric, disciplinaId, turmaId)).thenReturn(true);

        // Act
        BaseDTO result = controller.matriculaAluno(numMatric, disciplinaId, turmaId);

        // Assert
        assertTrue(result.sucess());
        assertEquals("Aluno matriculado com sucesso.", result.response());
    }

    @Test
    public void testMatriculaAluno_CadeiraNaoEncontrada() {
        // Arrange
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        Aluno aluno = new Aluno();
        aluno.setNum_matricula(numMatric);

        when(alunoRepo.getReferenceById(numMatric)).thenReturn(aluno);
        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(null);

        // Act
        BaseDTO result = controller.matriculaAluno(numMatric, disciplinaId, turmaId);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Não foi possível matricular o aluno", result.response());
    }

    @Test
    public void testMatriculaAluno_ExceptionThrown() {
        // Arrange
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        when(alunoRepo.getReferenceById(numMatric)).thenThrow(new RuntimeException("Erro ao obter aluno"));
        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(new Cadeira());

        // Act
        BaseDTO result = controller.matriculaAluno(numMatric, disciplinaId, turmaId);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Não foi possível matricular o aluno", result.response());
    }

    @Test
    public void testObtemCadeirasAluno_Success() {
        // Arrange
        long codMatric = 123456789;

        Aluno aluno = new Aluno();
        aluno.setMatriculas(Arrays.asList(new Cadeira(), new Cadeira()));

        when(alunoRepo.findById(codMatric)).thenReturn(Optional.of(aluno));

        // Act
        ListaMatriculaDTO result = controller.obtemCadeirasAluno(codMatric);

        // Assert
        assertTrue(result.sucesso());
        assertEquals(2, result.listaMatriculas().size());
    }

    @Test
    public void testObtemCadeirasAluno_AlunoNotFound() {
        // Arrange
        long codMatric = 987654321;

        when(alunoRepo.findById(codMatric)).thenReturn(Optional.empty());

        // Act
        ListaMatriculaDTO result = controller.obtemCadeirasAluno(codMatric);

        // Assert
        assertFalse(result.sucesso());
        assertNull(result.listaMatriculas());
    }

    @Test
    public void testObtemCadeirasAluno_ExceptionThrown() {
        // Arrange
        long codMatric = 123456789;

        when(alunoRepo.findById(codMatric)).thenThrow(new RuntimeException("Erro ao obter aluno"));

        // Act
        ListaMatriculaDTO result = controller.obtemCadeirasAluno(codMatric);

        // Assert
        assertFalse(result.sucesso());
        assertNull(result.listaMatriculas());
    }

    @Test
    public void testObtemAlunos_AlunosEncontrados() {
        // Arrange
        long disciplinaId = 1;
        long turmaId = 1;
        List<Aluno> alunos = Arrays.asList(new Aluno(), new Aluno());

        Cadeira cadeira = new Cadeira();
        cadeira.setAlunos(alunos);

        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        // Act
        BaseDTO result = controller.obtemAlunos(disciplinaId, turmaId);

        // Assert
        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    // método trocado no controller para retornar false na excessão
    @Test
    public void testObtemAlunos_CadeiraNaoEncontrada() {
        // Arrange
        long disciplinaId = 2;
        long turmaId = 2;

        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(null);

        // Act
        BaseDTO result = controller.obtemAlunos(1, 1);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Alunos não encontrados para esta cadeira.", result.response());
    }

    @Test
    public void testObtemAlunos_ListaAlunosVazia() {
        // Arrange
        long disciplinaId = 3;
        long turmaId = 3;
        Cadeira cadeira = new Cadeira();
        cadeira.setAlunos(new ArrayList<>());

        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        // Act
        BaseDTO result = controller.obtemAlunos(disciplinaId, turmaId);

        // Assert
        assertFalse(result.sucess());
    }

    @Test
    public void testObtemAlunos_ExcecaoBuscarCadeira() {
        // Arrange
        long disciplinaId = 4;
        long turmaId = 4;

        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenThrow(new RuntimeException("Erro ao buscar cadeira."));

        // Act
        BaseDTO result = controller.obtemAlunos(disciplinaId, turmaId);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Erro ao buscar cadeira.", result.response());
    }

    // Incluida validação no controlador caso a lista esteja vazia
    @Test
    public void testObtemAlunos_ExcecaoBuscarAlunos() {
        // Arrange
        long disciplinaId = 5;
        long turmaId = 5;
        Cadeira cadeira = new Cadeira();

        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        // Act
        BaseDTO result = controller.obtemAlunos(disciplinaId, turmaId);

        // Assert
        assertFalse(result.sucess());
        assertEquals("Alunos não encontrados para esta cadeira.", result.response());
    }


}
