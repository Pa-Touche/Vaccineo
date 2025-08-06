package lu.vaccineo;

import com.vaadin.spring.annotation.EnableVaadin;
import lu.vaccineo.technical.security.JwtValidationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableVaadin
@EnableConfigurationProperties
@EnableScheduling
public class VaccineoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaccineoApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<JwtValidationFilter> jwtValidationFilterRegistrationBean(JwtValidationFilter jwtValidationFilter) {
        FilterRegistrationBean<JwtValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtValidationFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.FRENCH); // Default locale
        return slr;
    }

}
