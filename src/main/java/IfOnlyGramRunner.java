import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ifonlygram")
public class IfOnlyGramRunner {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(IfOnlyGramRunner.class, args);
    }
}
