package koo.real.spring.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserFindService {

    private final UserRepository userRepository;

    public boolean existsByEmail(final String email) { // 이미 소셜 로그인을 통해 가입된 이메일이 존재하는지 확인
        return userRepository.existsByEmail(email);
    }

}
