package study.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.springsecurity.domain.Role;
import study.springsecurity.domain.User;
import study.springsecurity.dto.JoinDTO;
import study.springsecurity.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO){

        // username 중복 검증
        if(userRepository.existsByUsername(joinDTO.getUsername())){
            return;
        }

        User user = new User();
        user.setUsername(joinDTO.getUsername());

        // 암호화 진행 (joinDTO로 입력받은 비밀번호를 encode()를 통해서 암호화)
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));

        // ROLE 접두사 필수
        user.setRole(Role.USER);

        userRepository.save(user);
    }
}
