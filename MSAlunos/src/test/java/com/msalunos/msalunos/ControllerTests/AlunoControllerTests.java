package com.msalunos.msalunos.ControllerTests;
import com.ms.common.DTO.BaseDTO;
import com.ms.common.DTO.ListaMatriculaDTO;
import com.msalunos.msalunos.Controller.AlunoController;
import com.msalunos.msalunos.DTO.DadosCadastroAlunoDTO;
import com.msalunos.msalunos.Model.Aluno;
import com.msalunos.msalunos.Model.Cadeira;
import com.msalunos.msalunos.Repository.IRepoAluno;
import com.msalunos.msalunos.Repository.IRepoCadeira;
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
public class AlunoControllerTests {
    @Mock
    private IRepoAluno iRepoAluno;

    @Mock
    private IRepoCadeira iRepoCadeira;

    @Mock
    private AlunoService alunoService;

    @InjectMocks
    private AlunoController controller;

    @Test
    public void testAdicionaAluno_Success() {
       
        DadosCadastroAlunoDTO dto = new DadosCadastroAlunoDTO("John Doe", "123.112.222.32","rua chico pedor" );

        Aluno aluno = new Aluno();
        BeanUtils.copyProperties(dto, aluno);

        when(iRepoAluno.save(any(Aluno.class))).thenReturn(aluno);


        BaseDTO result = controller.adicionarAluno(dto);

        assertTrue(result.sucess());
        assertEquals("Aluno cadastrado com sucesso.", result.response());
    }

    @Test
    public void testAdicionaAluno_Failure() {
       
        DadosCadastroAlunoDTO dto = new DadosCadastroAlunoDTO("jon dee", "111.111.111-11", "efwef");

        when(iRepoAluno.save(any(Aluno.class))).thenThrow(new RuntimeException("Erro ao cadastrar o aluno."));


        BaseDTO result = controller.adicionarAluno(dto);

        assertFalse(result.sucess());
        assertEquals("Erro ao cadastrar o aluno.", result.response());
    }

    @Test
    public void testGetAlunosPorNome_Success() {
       
        String nome = "John Doe";

        List<Aluno> alunosPorNome = new ArrayList<>();
        Aluno a = new Aluno();
        a.setNome("John Doe");
        alunosPorNome.add(a);

        when(alunoService.getAlunosPorNome(anyString())).thenReturn(alunosPorNome);


        BaseDTO result = controller.listarAlunosPorNome(nome);

        assertTrue(result.sucess());
        assertEquals(alunosPorNome, result.response());
    }

    @Test
    public void testGetAlunosPorNome_NoMatchingAlunos() {
       
        String nome = "Jane Smith";

        when(alunoService.getAlunosPorNome(anyString())).thenReturn(new ArrayList<>());


        BaseDTO result = controller.listarAlunosPorNome(nome);

        assertFalse(result.sucess());
        assertEquals("Não foram encontrados alunos com este nome.", result.response());
    }

    @Test
    public void testGetAlunosPorNome_NoMatchingAlunos_parte_nome() {
       
        String nome = "Jane";
        Aluno jane = new Aluno();
        Aluno janeSmith = new Aluno();
        jane.setNome("Jane Smith");
        janeSmith.setNome("Jane Smith feijó");
        List<Aluno> alunos = Arrays.asList(jane, janeSmith);
        when(alunoService.getAlunosPorNome(anyString())).thenReturn(alunos);


        BaseDTO result = controller.listarAlunosPorNome(nome);

        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testListaTodosAlunos_Success() {
       
        List<Aluno> alunos = Arrays.asList(new Aluno(), new Aluno(), new Aluno());
        when(iRepoAluno.findAll()).thenReturn(alunos);


        BaseDTO result = controller.listarTodosAlunos();

        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    @Test
    public void testListaTodosAlunos_NoAlunosCadastrados() {
       
        when(iRepoAluno.findAll()).thenReturn(new ArrayList<>());


        BaseDTO result = controller.listarTodosAlunos();

        assertFalse(result.sucess());
        assertEquals("Não há alunos cadastrados.", result.response());
    }

    @Test
    public void testMatriculaAluno_Success() {
       
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        Aluno aluno = new Aluno();
        aluno.setNum_matricula(numMatric);

        Cadeira cadeira = new Cadeira();
        cadeira.setId(disciplinaId);
        cadeira.setTurma_id(turmaId);

        when(alunoService.matricularAluno(numMatric, disciplinaId, turmaId)).thenReturn(true);

        BaseDTO result = controller.matricularAluno(numMatric, disciplinaId, turmaId);

        assertTrue(result.sucess());
        assertEquals("Aluno matriculado com sucesso.", result.response());
    }

    @Test
    public void testMatriculaAluno_CadeiraNaoEncontrada() {
       
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        Aluno aluno = new Aluno();
        aluno.setNum_matricula(numMatric);

        when(iRepoAluno.getReferenceById(numMatric)).thenReturn(aluno);
        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(null);

        BaseDTO result = controller.matricularAluno(numMatric, disciplinaId, turmaId);

        assertFalse(result.sucess());
        assertEquals("Não foi possível matricular o aluno", result.response());
    }

    @Test
    public void testMatriculaAluno_ExceptionThrown() {
       
        long numMatric = 123456789;
        long disciplinaId = 1;
        long turmaId = 1;

        when(iRepoAluno.getReferenceById(numMatric)).thenThrow(new RuntimeException("Erro ao obter aluno"));
        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(new Cadeira());

        BaseDTO result = controller.matricularAluno(numMatric, disciplinaId, turmaId);

        assertFalse(result.sucess());
        assertEquals("Não foi possível matricular o aluno", result.response());
    }

    @Test
    public void testObtemCadeirasAluno_Success() {
       
        long codMatric = 123456789;

        Aluno aluno = new Aluno();
        aluno.setMatriculas(Arrays.asList(new Cadeira(), new Cadeira()));

        when(iRepoAluno.findById(codMatric)).thenReturn(Optional.of(aluno));

        ListaMatriculaDTO result = controller.obterCadeirasAluno(codMatric);

        assertTrue(result.sucesso());
        assertEquals(2, result.listaMatriculas().size());
    }

    @Test
    public void testObtemCadeirasAluno_AlunoNotFound() {
       
        long codMatric = 987654321;

        when(iRepoAluno.findById(codMatric)).thenReturn(Optional.empty());

        ListaMatriculaDTO result = controller.obterCadeirasAluno(codMatric);

        assertFalse(result.sucesso());
        assertNull(result.listaMatriculas());
    }

    @Test
    public void testObtemCadeirasAluno_ExceptionThrown() {
       
        long codMatric = 123456789;

        when(iRepoAluno.findById(codMatric)).thenThrow(new RuntimeException("Erro ao obter aluno"));

        ListaMatriculaDTO result = controller.obterCadeirasAluno(codMatric);

        assertFalse(result.sucesso());
        assertNull(result.listaMatriculas());
    }

    @Test
    public void testObtemAlunos_AlunosEncontrados() {
       
        long disciplinaId = 1;
        long turmaId = 1;
        List<Aluno> alunos = Arrays.asList(new Aluno(), new Aluno());

        Cadeira cadeira = new Cadeira();
        cadeira.setAlunos(alunos);

        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        BaseDTO result = controller.obterAlunosPorCadeira(disciplinaId, turmaId);

        assertTrue(result.sucess());
        assertEquals(alunos, result.response());
    }

    // método trocado no controller para retornar false na excessão
    @Test
    public void testObtemAlunos_CadeiraNaoEncontrada() {
       
        long disciplinaId = 2;
        long turmaId = 2;

        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(null);

        BaseDTO result = controller.obterAlunosPorCadeira(1, 1);

        assertFalse(result.sucess());
        assertEquals("Alunos não encontrados para esta cadeira.", result.response());
    }

    @Test
    public void testObtemAlunos_ListaAlunosVazia() {
       
        long disciplinaId = 3;
        long turmaId = 3;
        Cadeira cadeira = new Cadeira();
        cadeira.setAlunos(new ArrayList<>());

        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        BaseDTO result = controller.obterAlunosPorCadeira(disciplinaId, turmaId);

        assertFalse(result.sucess());
    }

    @Test
    public void testObtemAlunos_ExcecaoBuscarCadeira() {
       
        long disciplinaId = 4;
        long turmaId = 4;

        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenThrow(new RuntimeException("Erro ao buscar cadeira."));

        BaseDTO result = controller.obterAlunosPorCadeira(disciplinaId, turmaId);

        assertFalse(result.sucess());
        assertEquals("Erro ao buscar cadeira.", result.response());
    }

    // Incluida validação no controlador caso a lista esteja vazia
    @Test
    public void testObtemAlunos_ExcecaoBuscarAlunos() {
       
        long disciplinaId = 5;
        long turmaId = 5;
        Cadeira cadeira = new Cadeira();

        when(iRepoCadeira.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        BaseDTO result = controller.obterAlunosPorCadeira(disciplinaId, turmaId);

        assertFalse(result.sucess());
        assertEquals("Alunos não encontrados para esta cadeira.", result.response());
    }

}
