package br.com.gerenciamento.acceptance;

import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioAcceptanceTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void iniciar() {
        this.usuarioRepository.deleteAll();
        this.driver = new ChromeDriver();
    }

    @After
    public void finalizar() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    public void cadastrarUsuarioComSucesso() {
        String email = "usuario" + System.currentTimeMillis() + "@email.com";
        String user = "user" + System.currentTimeMillis() % 100000;

        this.driver.get("http://localhost:" + port + "/cadastro");

        this.driver.findElement(By.name("user")).sendKeys(user);
        this.driver.findElement(By.name("email")).sendKeys(email);
        this.driver.findElement(By.name("senha")).sendKeys("123456");

        WebElement campoSenha = this.driver.findElement(By.name("senha"));
        campoSenha.submit();

        Assert.assertTrue(this.driver.getCurrentUrl().equals("http://localhost:" + port + "/"));

        Usuario usuarioRetorno = this.usuarioRepository.findByEmail(email);

        Assert.assertNotNull(usuarioRetorno);
        Assert.assertEquals(user, usuarioRetorno.getUser());
        Assert.assertEquals(email, usuarioRetorno.getEmail());
    }
}
