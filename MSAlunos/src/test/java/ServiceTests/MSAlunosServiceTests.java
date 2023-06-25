package ServiceTests;

import com.msalunos.msalunos.Model.Aluno;
import com.msalunos.msalunos.Model.Cadeira;
import com.msalunos.msalunos.Repository.AlunoRepo;
import com.msalunos.msalunos.Repository.CadeiraRepo;
import com.msalunos.msalunos.Service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class MSAlunosServiceTests {
    @Mock
    private AlunoRepo alunoRepo;

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private CadeiraRepo cadeiraRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAlunosPorNome_Success() {
        // Arrange
        String nome = "John";
        Aluno aluno1 = new Aluno();
        aluno1.setNome("John Doe");
        Aluno aluno2 = new Aluno();
        aluno2.setNome("John Smith");
        Aluno aluno3 = new Aluno();
        aluno3.setNome("Jane Doe");
        List<Aluno> alunos = Arrays.asList(aluno1, aluno2, aluno3);

        when(alunoRepo.findAll()).thenReturn(alunos);

        // Act
        List<Aluno> result = alunoService.getAlunosPorNome(nome);

        // Assert
        assertEquals(2, result.size());
        assertEquals(aluno1, result.get(0));
        assertEquals(aluno2, result.get(1));
    }

    @Test
    public void testGetAlunosPorNome_NoMatchingAlunos() {
        // Arrange
        String nome = "Jane";
        Aluno aluno1 = new Aluno();
        aluno1.setNome("John Doe");
        Aluno aluno2 = new Aluno();
        aluno2.setNome("John Smith");
        List<Aluno> alunos = Arrays.asList(aluno1, aluno2);

        when(alunoRepo.findAll()).thenReturn(alunos);

        // Act
        List<Aluno> result = alunoService.getAlunosPorNome(nome);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testGetAlunosPorNome_EmptyAlunosList() {
        // Arrange
        String nome = "John";

        when(alunoRepo.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Aluno> result = alunoService.getAlunosPorNome(nome);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testMatricularAluno_Success() {
        // Arrange
        long numMatricula = 1;
        long disciplinaId = 1;
        long turmaId = 1;
        Aluno aluno = new Aluno();
        aluno.setNum_matricula(numMatricula);
        Cadeira cadeira = new Cadeira();
        cadeira.setId(1L);
        cadeira.setDisciplina_id(disciplinaId);
        cadeira.setTurma_id(turmaId);

        when(alunoRepo.getReferenceById(numMatricula)).thenReturn(aluno);
        when(cadeiraRepo.findByFk(disciplinaId, turmaId)).thenReturn(cadeira);

        // Act
        boolean result = alunoService.matricularAluno(numMatricula, disciplinaId, turmaId);

        // Assert
        assertTrue(result);
        assertTrue(aluno.getMatriculas().contains(cadeira));
    }
}
