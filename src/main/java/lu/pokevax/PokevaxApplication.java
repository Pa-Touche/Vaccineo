package lu.pokevax;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableVaadin
@EnableConfigurationProperties
public class PokevaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokevaxApplication.class, args);
    }

}
