package lu.pokevax;

import com.vaadin.spring.annotation.EnableVaadin;
import lu.pokevax.technical.security.JwtValidationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableVaadin
@EnableConfigurationProperties
@EnableScheduling
public class PokevaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokevaxApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<JwtValidationFilter> jwtValidationFilterRegistrationBean(JwtValidationFilter jwtValidationFilter) {
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtValidationFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

}
