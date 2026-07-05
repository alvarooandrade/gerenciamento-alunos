package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private ServiceUsuario serviceUsuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void salvarUsuario() throws Exception {
        String email = "usuario" + System.currentTimeMillis() + "@email.com";
        String user = "user" + System.currentTimeMillis() % 100000;

        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha("123456");

        this.serviceUsuario.salvarUsuario(usuario);

        Usuario usuarioRetorno = this.usuarioRepository.findByEmail(email);

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals(email, usuarioRetorno.getEmail());
        Assert.assertEquals(user, usuarioRetorno.getUser());
    }

    @Test
    public void salvarUsuarioComEmailRepetido() throws Exception {
        String email = "repetido" + System.currentTimeMillis() + "@email.com";

        Usuario usuario1 = new Usuario();
        usuario1.setUser("joao");
        usuario1.setEmail(email);
        usuario1.setSenha("123456");

        this.serviceUsuario.salvarUsuario(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setUser("maria");
        usuario2.setEmail(email);
        usuario2.setSenha("654321");

        Assert.assertThrows(EmailExistsException.class, () -> {
            this.serviceUsuario.salvarUsuario(usuario2);
        });
    }

    @Test
    public void loginUserComSucesso() throws Exception {
        String email = "login" + System.currentTimeMillis() + "@email.com";
        String user = "login" + System.currentTimeMillis() % 100000;
        String senha = "123456";

        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha(senha);

        this.serviceUsuario.salvarUsuario(usuario);

        Usuario usuarioRetorno = this.serviceUsuario.loginUser(user, Util.md5(senha));

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals(user, usuarioRetorno.getUser());
        Assert.assertEquals(email, usuarioRetorno.getEmail());
    }

    @Test
    public void loginUserComSenhaIncorreta() throws Exception {
        String email = "senhaerrada" + System.currentTimeMillis() + "@email.com";
        String user = "erro" + System.currentTimeMillis() % 100000;

        Usuario usuario = new Usuario();
        usuario.setUser(user);
        usuario.setEmail(email);
        usuario.setSenha("123456");

        this.serviceUsuario.salvarUsuario(usuario);

        Usuario usuarioRetorno = this.serviceUsuario.loginUser(user, "senhaerrada");

        Assert.assertNull(usuarioRetorno);
    }

}
