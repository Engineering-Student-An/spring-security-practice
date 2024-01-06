package study.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import study.springsecurity.domain.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 시큐리티 필터 메서드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        // 인가 동작 순서 : 위에서 부터 아래로 순서대로 ! 따라서 순서 유의 (anyRequest 특히)

                        // hasRole => 로그인 이후에 특정 role이 있어야 접근 가능하게 설정
                        // permitAll => 로그인을 하지 않아도 모든 사용자가 접근 가능하게 설정
                        // authenticated => 로그인만 진행하면 모든 사용자가 접근 가능하게 설정
                        // denyAll => 로그인을 해도 모든 사용자의 접근 막게 설정
                        .requestMatchers("/", "/login", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole(Role.ADMIN.name())
                        // ** : 와일드카드, hasAnyRole => 여러 개의 role 설정 가능
                        .requestMatchers("/my/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        // anyRequest => 위에서 처리하지 못한 나머지 경로에 대한 처리
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        // 프론트단에서 설정해 둔 경로로 로그인 정보를 넘기면 스프링 시큐리티가 받아서 자동으로 로그인 진행
                        .permitAll()
                );


        // csrf : 사이트 위변조 방지 설정 (스프링 시큐리티에는 자동으로 설정 되어 있음)
        // csrf기능 켜져있으면 post 요청을 보낼때 csrf 토큰도 보내줘야 로그인 진행됨 !
        // 지금만 csrf 잠시 꺼두기
        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }

    // BCrypt password encoder를 리턴하는 메서드 => 회원가입할 때 암호화 진행하는 과정도 이 메서드 사용해서 진행!
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){


        return new BCryptPasswordEncoder();
    }
}
