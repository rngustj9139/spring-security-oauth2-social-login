package koo.real.spring.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService { // 회원가입 비즈니스 로직 구현

    private final UserFindService userFindService;
    private final UserRepository userRepository;

    public void requestRegistration(final String name, final String email) {
        final boolean exists = userFindService.existsByEmail(email);

        if(exists == false) { // 신규 가입인 경우
            final User user = new User(name, email);
            userRepository.save(user);
        }
    }

}
