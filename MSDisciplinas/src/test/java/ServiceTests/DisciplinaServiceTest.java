package ServiceTests;

import com.msdisciplinas.msdisciplinas.ENUM.CadastroDisciplinaEnum;
import com.msdisciplinas.msdisciplinas.Model.Disciplina;
import com.msdisciplinas.msdisciplinas.Model.Turma;
import com.msdisciplinas.msdisciplinas.Repository.IRepoDisciplina;
import com.msdisciplinas.msdisciplinas.Repository.IRepoTurma;
import com.msdisciplinas.msdisciplinas.Service.DisciplinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class DisciplinaServiceTest {

    @Mock
    private IRepoDisciplina iRepoDisciplina;

    @Mock
    private IRepoTurma iRepoTurma;

    @InjectMocks
    private DisciplinaService disciplinaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAdicionaDisciplinaComTurmaValida() {
        // Arrange
        Disciplina disciplina = new Disciplina();
        disciplina.setId(1);
        disciplina.setCodigo_disciplina(123);
        disciplina.setNome("Matemática");
        disciplina.setHorario("A");

        long codTurma = 1001;

        Turma turma = new Turma();
        turma.setId(1);
        turma.setCod_turma(codTurma);

        disciplina.setTurmas(new ArrayList<>());
        disciplina.getTurmas().add(turma);
        Optional<Turma> optionalTurma = Optional.of(turma);

        when(iRepoTurma.findByCode(codTurma)).thenReturn(turma);
        when(iRepoTurma.getReferenceById(turma.getId())).thenReturn(turma);
        when(iRepoDisciplina.save(disciplina)).thenReturn(disciplina);

        // Act
        CadastroDisciplinaEnum resultado = disciplinaService.adicionarDisciplina(disciplina, codTurma);

        // Assert
        assertEquals(CadastroDisciplinaEnum.ADICIONOU, resultado);
    }

    @Test
    public void testAdicionaDisciplinaComTurmaInexistente() {
        // Arrange
        Disciplina disciplina = new Disciplina();
        disciplina.setId(2);
        disciplina.setCodigo_disciplina(456);
        disciplina.setNome("História");
        disciplina.setHorario("G");

        long codTurma = 9999;

        when(iRepoTurma.findByCode(codTurma)).thenReturn(null);

        // Act
        CadastroDisciplinaEnum resultado = disciplinaService.adicionarDisciplina(disciplina, codTurma);

        // Assert
        assertEquals(CadastroDisciplinaEnum.SEM_TURMAS, resultado);
    }

}
