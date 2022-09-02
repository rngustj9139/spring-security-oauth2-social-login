package koo.real.spring.security.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final String registration = "spring.security.oauth2.client.registration."; // application.yml 파일에 있는 데이터를 가져올 것임

    @Override
    protected void configure(HttpSecurity http) throws Exception { // Authorization Code Grant Type 이용
        http.authorizeRequests(authorize -> authorize
                        .antMatchers("/login", "/index").permitAll()
                        .anyRequest().authenticated()
        ).oauth2Login(Customizer.withDefaults()); // oauth2는 디폴트 설정을 하겠다. => localhost:8081로 접속하면 저절로 localhost:8081/login으로 리다이렉트 된다.
        // 위의 auto configuration이 아닌 아래 코드(커스텀 설정 코드)를 쓴다면
        /**
         * .oauth2Login(oauth2 -> oauth2
         *      .clientRegistrationRepository(clientRegistrationRepository())
         *      .authorizedClientService(authorizedClientService())
         * );
         * **/
        // Customizer.withDefaults() 대신 위의 코드를 써야한다.
    }

    // 커스텀 설정 시작(Below All Codes) => google, facebook이 아닌 카카오나 네이버 같은 소셜 로그인을 이용할려면 커스텀 설정을 해야한다.
    private ClientRegistration googleClientRegistration() {
        final String clientId = environment.getProperty(registration + "google.client-id");
        final String clientSecret = environment.getProperty(registration + "google.client-secret");

        return CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    private ClientRegistration facebookClientRegistration() {
        final String clientId = environment.getProperty(registration + "facebook.client-id");
        final String clientSecret = environment.getProperty(registration + "facebook.client-secret");

        return CommonOAuth2Provider
                .FACEBOOK
                .getBuilder("facebook")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() { // 위에서 만든 ClientRegistration들을 저장할 Repository를 정의
        final List<ClientRegistration> clientRegistrations = Arrays.asList(
            googleClientRegistration(),
            facebookClientRegistration()
        );

        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() { // 실제로 로그인이 수행되는 함수
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

}
