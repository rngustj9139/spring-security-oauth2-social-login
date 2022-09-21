package koo.real.spring.security.oauth2.config;

import koo.real.spring.security.oauth2.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacebookOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRegistrationService userRegistrationService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { // 회원 정보를 가져옴
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = userService.loadUser(userRequest);

        final String name = oAuth2User.getAttributes().get("name").toString(); // 사용자 정보 가져오기 (이름)
        final String email = oAuth2User.getAttributes().get("email").toString(); // 사용자 정보 가져오기 (이메일)

        userRegistrationService.requestRegistration(name, email); // 이미 가입한 정보가 아니라면 회원가입 기능 자동 수행

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "id");
    }

}
