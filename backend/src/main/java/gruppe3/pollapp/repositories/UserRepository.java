package gruppe3.pollapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gruppe3.pollapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Sjekk ut:
    // https://docs.spring.io/spring-data/jpa/reference/jpa/getting-started.html

    // Disse metodenavnene er spesifikke, og vil automatisk gjøres om
    // til queries.
    // Les mulige metodenavn her:
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-keywords-reference.html
    User save(User user);

    Optional<User> findById(long id);

    User findByUsername(String username);
}
