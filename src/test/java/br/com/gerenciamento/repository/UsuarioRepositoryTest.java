package br.com.gerenciamento.repository;

import br.com.gerenciamento.model.Usuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void limparBanco() {
        this.usuarioRepository.deleteAll();
    }

    @Test
    public void buscarLogin() {
        Usuario usuario = criarUsuario("richy", "richy@email.com", "123456");

        this.usuarioRepository.save(usuario);

        Usuario usuarioRetorno = this.usuarioRepository.buscarLogin("richy", "123456");

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals("richy", usuarioRetorno.getUser());
        Assert.assertEquals("richy@email.com", usuarioRetorno.getEmail());
    }

    @Test
    public void buscarLoginComSenhaIncorreta() {
        Usuario usuario = criarUsuario("maria", "maria@email.com", "123456");

        this.usuarioRepository.save(usuario);

        Usuario usuarioRetorno = this.usuarioRepository.buscarLogin("maria", "senhaerrada");

        Assert.assertNull(usuarioRetorno);
    }

    @Test
    public void findByEmail() {
        Usuario usuario = criarUsuario("pedro", "pedro@email.com", "123456");

        this.usuarioRepository.save(usuario);

        Usuario usuarioRetorno = this.usuarioRepository.findByEmail("pedro@email.com");

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals("pedro@email.com", usuarioRetorno.getEmail());
        Assert.assertEquals("pedro", usuarioRetorno.getUser());
    }

    @Test
    public void salvarUsuario() {
        Usuario usuario = criarUsuario("ana", "ana@email.com", "123456");

        Usuario usuarioSalvo = this.usuarioRepository.save(usuario);

        Assert.assertNotNull(usuarioSalvo.getId());
        Assert.assertEquals("ana", usuarioSalvo.getUser());
        Assert.assertEquals("ana@email.com", usuarioSalvo.getEmail());
    }

    private Usuario criarUsuario(String user, String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }
}
