package study.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springsecurity.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

}
