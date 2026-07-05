package br.com.gerenciamento.controller;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import br.com.gerenciamento.repository.AlunoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void limparBanco() {
        this.alunoRepository.deleteAll();
    }

    @Test
    public void pesquisarAlunoComNomeVazioRetornaTodosAlunos() throws Exception {
        Aluno aluno1 = criarAluno("Nilton Richy", Status.ATIVO, "111111");
        Aluno aluno2 = criarAluno("Herica Silva", Status.INATIVO, "222222");

        this.alunoRepository.save(aluno1);
        this.alunoRepository.save(aluno2);

        this.mockMvc.perform(post("/pesquisar-aluno")
                        .param("nome", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/pesquisa-resultado"))
                .andExpect(model().attributeExists("ListaDeAlunos"))
                .andExpect(model().attribute("ListaDeAlunos", hasSize(2)));
    }

    @Test
    public void pesquisarAlunoComNomeRetornaApenasAlunosEncontrados() throws Exception {
        Aluno aluno1 = criarAluno("Carlos Eduardo", Status.ATIVO, "333333");
        Aluno aluno2 = criarAluno("Maria Silva", Status.ATIVO, "444444");

        this.alunoRepository.save(aluno1);
        this.alunoRepository.save(aluno2);

        this.mockMvc.perform(post("/pesquisar-aluno")
                        .param("nome", "carlos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/pesquisa-resultado"))
                .andExpect(model().attributeExists("ListaDeAlunos"))
                .andExpect(model().attribute("ListaDeAlunos", hasSize(1)));
    }

    @Test
    public void listagemAlunosRetornaListaDeAlunos() throws Exception {
        Aluno aluno1 = criarAluno("Aluno Um", Status.ATIVO, "555555");
        Aluno aluno2 = criarAluno("Aluno Dois", Status.INATIVO, "666666");

        this.alunoRepository.save(aluno1);
        this.alunoRepository.save(aluno2);

        this.mockMvc.perform(get("/alunos-adicionados"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/listAlunos"))
                .andExpect(model().attributeExists("alunosList"))
                .andExpect(model().attribute("alunosList", hasSize(2)));
    }

    @Test
    public void listaAlunosAtivosRetornaSomenteAtivos() throws Exception {
        Aluno alunoAtivo = criarAluno("Aluno Ativo", Status.ATIVO, "777777");
        Aluno alunoInativo = criarAluno("Aluno Inativo", Status.INATIVO, "888888");

        this.alunoRepository.save(alunoAtivo);
        this.alunoRepository.save(alunoInativo);

        this.mockMvc.perform(get("/alunos-ativos"))
                .andExpect(status().isOk())
                .andExpect(view().name("Aluno/alunos-ativos"))
                .andExpect(model().attributeExists("alunosAtivos"))
                .andExpect(model().attribute("alunosAtivos", hasSize(1)));
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
