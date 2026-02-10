import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot entry point for the endterm project API.
 *
 * This uses simple package names (controller, service, repository, model, etc.)
 * so it integrates smoothly with the existing code from previous assignments.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "controller",
        "service",
        "repository",
        "model",
        "dto",
        "exception",
        "patterns",
        "utils",
        "config",
        "logging"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

