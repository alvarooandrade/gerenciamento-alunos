package br.com.gerenciamento.acceptance;

import br.com.gerenciamento.repository.AlunoRepository;
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
public class AlunoAcceptanceTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private AlunoRepository alunoRepository;

    @Before
    public void iniciar() {
        this.alunoRepository.deleteAll();
        this.driver = new ChromeDriver();
    }

    @After
    public void finalizar() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    @Test
    public void cadastrarAlunoComSucesso() {
        this.driver.get("http://localhost:" + port + "/inserirAlunos");

        this.driver.findElement(By.name("nome")).sendKeys("Aluno Aceitacao");
        this.driver.findElement(By.name("matricula")).sendKeys("999999");
        this.driver.findElement(By.name("turno")).sendKeys("NOTURNO");
        this.driver.findElement(By.name("curso")).sendKeys("ADMINISTRACAO");
        this.driver.findElement(By.name("status")).sendKeys("ATIVO");

        WebElement campoStatus = this.driver.findElement(By.name("status"));
        campoStatus.submit();

        Assert.assertTrue(this.driver.getCurrentUrl().contains("/alunos-adicionados"));
        Assert.assertFalse(this.alunoRepository.findByNomeContainingIgnoreCase("Aluno Aceitacao").isEmpty());
    }
}
