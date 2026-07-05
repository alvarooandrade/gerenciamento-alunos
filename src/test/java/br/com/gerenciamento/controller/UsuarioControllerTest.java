package br.com.gerenciamento.controller;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void limparBanco() {
        this.usuarioRepository.deleteAll();
    }

    @Test
    public void salvarUsuarioRedirecionaParaLogin() throws Exception {
        String email = "usuario" + System.currentTimeMillis() + "@email.com";
        String user = "user" + System.currentTimeMillis() % 100000;

        this.mockMvc.perform(post("/salvarUsuario")
                        .param("user", user)
                        .param("email", email)
                        .param("senha", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Usuario usuarioRetorno = this.usuarioRepository.findByEmail(email);

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals(user, usuarioRetorno.getUser());
    }

    @Test
    public void cadastroRetornaTelaCadastro() throws Exception {
        this.mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void loginRetornaTelaLogin() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    public void loginComCredenciaisInvalidasNaoRedirecionaParaIndex() throws Exception {
        this.mockMvc.perform(post("/login")
                        .param("user", "usuarioinvalido")
                        .param("senha", "senhaerrada"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/cadastro"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(result -> Assert.assertNull(result.getResponse().getRedirectedUrl()));
    }
}
