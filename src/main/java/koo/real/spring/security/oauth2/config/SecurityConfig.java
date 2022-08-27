package koo.real.spring.security.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                        .antMatchers("/login", "/index").permitAll()
                        .anyRequest().authenticated()
        ).oauth2Login(Customizer.withDefaults()); // oauth2는 디폴트 설정을 하겠다.
    }

}
