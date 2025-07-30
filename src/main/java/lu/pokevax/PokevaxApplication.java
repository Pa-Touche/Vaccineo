package lu.pokevax;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin
public class PokevaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokevaxApplication.class, args);
    }

}
