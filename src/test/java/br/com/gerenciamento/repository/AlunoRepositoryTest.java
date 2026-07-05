package br.com.gerenciamento.repository;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {
    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void limparBanco() {
        this.alunoRepository.deleteAll();
    }

    @Test
    public void findByStatusInativo() {
        Aluno alunoAtivo = criarAluno("Aluno Ativo", Status.ATIVO, "111111");
        Aluno alunoInativo = criarAluno("Aluno Inativo", Status.INATIVO, "222222");

        this.alunoRepository.save(alunoAtivo);
        this.alunoRepository.save(alunoInativo);

        List<Aluno> alunosInativos = this.alunoRepository.findByStatusInativo();

        Assert.assertEquals(1, alunosInativos.size());
        Assert.assertEquals(Status.INATIVO, alunosInativos.get(0).getStatus());
        Assert.assertEquals("Aluno Inativo", alunosInativos.get(0).getNome());
    }

    @Test
    public void findByStatusAtivo() {
        Aluno alunoAtivo = criarAluno("Aluno Ativo", Status.ATIVO, "333333");
        Aluno alunoInativo = criarAluno("Aluno Inativo", Status.INATIVO, "444444");

        this.alunoRepository.save(alunoAtivo);
        this.alunoRepository.save(alunoInativo);

        List<Aluno> alunosAtivos = this.alunoRepository.findByStatusAtivo();

        Assert.assertEquals(1, alunosAtivos.size());
        Assert.assertEquals(Status.ATIVO, alunosAtivos.get(0).getStatus());
        Assert.assertEquals("Aluno Ativo", alunosAtivos.get(0).getNome());
    }

    @Test
    public void findByNomeContainingIgnoreCase() {
        Aluno alunoCarlos = criarAluno("Álvaro Henrique", Status.ATIVO, "555555");
        Aluno alunoMaria = criarAluno("Maria Silva", Status.ATIVO, "666666");

        this.alunoRepository.save(alunoCarlos);
        this.alunoRepository.save(alunoMaria);

        List<Aluno> alunos = this.alunoRepository.findByNomeContainingIgnoreCase("álvaro");

        Assert.assertEquals(1, alunos.size());
        Assert.assertEquals("Álvaro Henrique", alunos.get(0).getNome());
    }

    @Test
    public void salvarAluno() {
        Aluno aluno = criarAluno("Aluno Salvar", Status.ATIVO, "777777");

        Aluno alunoSalvo = this.alunoRepository.save(aluno);

        Aluno alunoRetorno = this.alunoRepository.findById(alunoSalvo.getId()).get();

        Assert.assertNotNull(alunoRetorno);
        Assert.assertEquals("Aluno Salvar", alunoRetorno.getNome());
        Assert.assertEquals(Status.ATIVO, alunoRetorno.getStatus());
    }

    private Aluno criarAluno(String nome, Status status, String matricula) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(status);
        aluno.setMatricula(matricula);
        return aluno;
    }
}
